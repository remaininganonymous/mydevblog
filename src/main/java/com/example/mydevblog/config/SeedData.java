package com.example.mydevblog.config;

import com.example.mydevblog.models.Account;
import com.example.mydevblog.models.Authority;
import com.example.mydevblog.models.Comment;
import com.example.mydevblog.models.Post;
import com.example.mydevblog.repositories.AuthorityRepository;
import com.example.mydevblog.services.AccountService;
import com.example.mydevblog.services.CommentService;
import com.example.mydevblog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CommentService commentService;

    @Override
    public void run(String... args) throws Exception {
        List<Post> posts = postService.getAll();

        if (posts.size() == 0) {
            Authority user = new Authority();
            user.setName("ROLE_USER");
            authorityRepository.save(user);

            Authority admin = new Authority();
            admin.setName("ROLE_ADMIN");
            authorityRepository.save(admin);

            Account account1 = new Account();
            Account account2 = new Account();

            account1.setFirstName("user_first");
            account1.setLastName("user_last");
            account1.setEmail("user.user@domain.com");
            account1.setPassword("password");
            Set<Authority> authorities1 = new HashSet<>();
            authorityRepository.findById("ROLE_USER").ifPresent(authorities1::add);
            account1.setAuthorities(authorities1);


            account2.setFirstName("admin_first");
            account2.setLastName("admin_last");
            account2.setEmail("admin.admin@domain.com");
            account2.setPassword("password");
            Set<Authority> authorities2 = new HashSet<>();
            authorityRepository.findById("ROLE_ADMIN").ifPresent(authorities2::add);
            authorityRepository.findById("ROLE_USER").ifPresent(authorities2::add);
            account2.setAuthorities(authorities2);

            accountService.save(account1);
            accountService.save(account2);

            Post post1 = new Post();
            post1.setTitle("What is Lorem Ipsum?");
            post1.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
            post1.setAccount(account1);

            Post post2 = new Post();
            post2.setTitle("Something else Ipsum");
            post2.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Magna eget est lorem ipsum dolor sit amet consectetur adipiscing. Tempus quam pellentesque nec nam aliquam sem et tortor. Pellentesque sit amet porttitor eget. Sed augue lacus viverra vitae congue eu consequat. Ultrices vitae auctor eu augue. Mattis rhoncus urna neque viverra. Consectetur lorem donec massa sapien faucibus et molestie ac feugiat. Sociis natoque penatibus et magnis dis parturient montes nascetur. Cursus turpis massa tincidunt dui ut ornare lectus. Odio pellentesque diam volutpat commodo sed egestas egestas fringilla. Id cursus metus aliquam eleifend mi. Nibh nisl condimentum id venenatis a condimentum.");
            post2.setAccount(account2);

            postService.save(post1);
            postService.save(post2);

            Comment comment1 = new Comment();
            comment1.setPost(post1);
            comment1.setBody("Some comment 1");
            comment1.setAccount(account1);
            Comment comment2 = new Comment();
            comment2.setPost(post1);
            comment2.setBody("Some comment 2");
            comment2.setAccount(account1);
            Comment comment3 = new Comment();
            comment3.setPost(post1);
            comment3.setBody("Some comment 3");
            comment3.setAccount(account2);

            commentService.save(comment1);
            commentService.save(comment2);
            commentService.save(comment3);
        }

        /*if (posts.size() == 0) {
            Account account1 = new Account();
            Account account2 = new Account();

            account1.setFirstName("user_first");
            account1.setLastName("user_last");
            account1.setEmail("user.user@domain.com");
            account1.setPassword("password");
            Set<Authority> authorities1 = new HashSet<>();
            authorityRepository.findById("ROLE_USER").ifPresent(authorities1::add);
            account1.setAuthorities(authorities1);

            account2.setFirstName("admin_first");
            account2.setLastName("admin_last");
            account2.setEmail("admin.admin@domain.com");
            account2.setPassword("password");

            accountService.save(account1);
            accountService.save(account2);

            Post post1 = new Post();
            post1.setTitle("title of post 1");
            post1.setBody("this is the body of post 1");
            post1.setAccount(account1);

            Post post2 = new Post();
            post2.setTitle("title of post 2");
            post2.setBody("this is the body of post 2");
            post2.setAccount(account2);

            postService.save(post1);
            postService.save(post2);
        }*/
    }
}
