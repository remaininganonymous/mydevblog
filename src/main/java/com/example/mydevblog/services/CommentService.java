package com.example.mydevblog.services;

import com.example.mydevblog.models.Comment;
import com.example.mydevblog.models.Post;
import com.example.mydevblog.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getById(Long id) {

        return commentRepository.findById(id);
    }
    public List<Comment> getByPost(Post post) {

        return commentRepository.findByPost(post);
    }

    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            comment.setWrittenAt(LocalDateTime.now());
        }
        System.out.println(comment.getId() + " " + comment.getBody());
        return commentRepository.save(comment);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public List<Comment> getAllCommentsForPost(Post updatedPost) {
        return commentRepository.findAllByPost(updatedPost);
    }
}
