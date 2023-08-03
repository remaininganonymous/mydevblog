package com.example.mydevblog.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(columnDefinition = "TEXT")
    private String body;

    private LocalDateTime writtenAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private Account account;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    private Post post;

    /*private Long postId;

    {
        postId = post.getId();
    }*/

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", body='" + body + "'" +
                ", createdAt='" + writtenAt + "'" +
                "}";
    }

}
