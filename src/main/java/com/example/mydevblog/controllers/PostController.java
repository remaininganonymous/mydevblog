package com.example.mydevblog.controllers;

import com.example.mydevblog.models.Account;
import com.example.mydevblog.models.Comment;
import com.example.mydevblog.models.Post;
import com.example.mydevblog.services.AccountService;
import com.example.mydevblog.services.CommentService;
import com.example.mydevblog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            List<Comment> comments = null;
            if (post != null) comments = commentService.getByPost(post);
            model.addAttribute("post", post);
            Comment comment = new Comment();
            model.addAttribute("new_comment", comment);
            model.addAttribute("comments", comments);
            return "post";
        } else {
            return "404";
        }
    }

    @PostMapping(value = "/posts/{id}/update")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post, BindingResult result, Model model) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());
            postService.save(existingPost);
        }

        return "redirect:/posts/" + post.getId();
    }

    @PostMapping(value = "/posts/{id}/comment")
    @PreAuthorize("isAuthenticated()")
    public String submitComment(@PathVariable Long id, @ModelAttribute Comment new_comment, Model model, Authentication authentication) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            new_comment.setWrittenAt(LocalDateTime.now());

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<Account> optionalAccount = accountService.findByEmail(userDetails.getUsername());
            if (optionalAccount.isPresent()) {
                Account account  = optionalAccount.get();
                new_comment.setAccount(account);
            } else {
                // Обработка случая, когда аккаунт для аутентифицированного пользователя не найден
                // Можно выбрать, как обрабатывать этот случай в зависимости от вашей логики
            }

            new_comment.setPost(post);
            new_comment.setId(Long.valueOf(commentService.getAll().size() + 1));
            commentService.save(new_comment);
        }

        return "redirect:/posts/" + id;
    }



    @PostMapping("/posts/{postId}/delete/{commentId}")
    @PreAuthorize("isAuthenticated()") /////
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<Account> optionalAccount = accountService.findByEmail(userDetails.getUsername());
        Optional<Comment> optionalComment = commentService.getById(commentId);
        Comment comment = optionalComment.get();
        if (optionalComment.isPresent()) {
            if (comment.getAccount() == optionalAccount.get()) commentService.delete(comment);
        }
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/posts/new")
    public String createNewPost(Model model) {
        Optional<Account> optionalAccount = accountService.findByEmail("user.user@domain.com"); //исправить
        if (optionalAccount.isPresent()) {
            Post post = new Post();
            post.setAccount(optionalAccount.get());
            model.addAttribute("post", post);
            return "post_new";
        } else {
            return "404";
        }
    }

    @PostMapping("/posts/new")
    public String saveNewPost(@ModelAttribute Post post) {
        postService.save(post);
        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getPostForEdit(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_edit";
        } else {
            return "404";
        }
    }

    @GetMapping("/posts/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deletePost(@PathVariable Long id) {

        // find post by id
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            postService.delete(post);
            return "redirect:/";
        } else {
            return "404";
        }
    }
}
