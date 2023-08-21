package com.example.mydevblog.controllers;


import com.example.mydevblog.models.Post;
import com.example.mydevblog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postService.getAll();

        // Sort the posts in reverse order (newest first)
        Collections.sort(posts, (post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt()));

        model.addAttribute("posts", posts);
        return "home";
    }
}
