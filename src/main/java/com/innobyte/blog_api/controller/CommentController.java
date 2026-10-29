package com.innobyte.blog_api.controller;

import com.innobyte.blog_api.dto.CommentRequest;
import com.innobyte.blog_api.entity.Comment;
import com.innobyte.blog_api.entity.Post;
import com.innobyte.blog_api.entity.User;
import com.innobyte.blog_api.repository.CommentRepository;
import com.innobyte.blog_api.repository.PostRepository;
import com.innobyte.blog_api.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentController(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    private Map<String, Object> toMap(Comment c) {
        return Map.of(
                "id", c.getId(),
                "content", c.getContent(),
                "postId", c.getPost().getId(),
                "author", c.getAuthor().getUsername(),
                "createdAt", c.getCreatedAt().toString()
        );
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {

        Post post = postRepository.findById(request.getPostId())
                .orElse(null);

        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Post not found"));
        }

        User author = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setPost(post);
        comment.setAuthor(author);

        Comment saved = commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toMap(saved));
    }

    @GetMapping
    public Object getComments(
            @RequestParam(name = "post_id", required = false)
            Long postId) {

        List<Comment> comments;

        if (postId != null) {
            comments = commentRepository
                    .findByPostIdOrderByCreatedAtAsc(postId);
        } else {
            comments = commentRepository.findAll();
        }

        return comments.stream().map(this::toMap).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return commentRepository.findById(id)
                .<ResponseEntity<?>>map(c ->
                        ResponseEntity.ok(toMap(c)))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of(
                                        "message", "Comment not found")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {

        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Comment not found"));
        }

        if (!comment.getAuthor().getUsername()
                .equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "You cannot edit this comment"));
        }

        if (!comment.getPost().getId().equals(request.getPostId())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message",
                            "Comment cannot be moved to another post"));
        }

        comment.setContent(request.getContent());

        return ResponseEntity.ok(
                toMap(commentRepository.save(comment)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            Authentication authentication) {

        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Comment not found"));
        }

        if (!comment.getAuthor().getUsername()
                .equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message",
                            "You cannot delete this comment"));
        }

        commentRepository.delete(comment);

        return ResponseEntity.noContent().build();
    }
}