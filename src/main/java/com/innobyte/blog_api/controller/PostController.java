package com.innobyte.blog_api.controller;

import com.innobyte.blog_api.dto.PostRequest;
import com.innobyte.blog_api.entity.Post;
import com.innobyte.blog_api.entity.User;
import com.innobyte.blog_api.repository.PostRepository;
import com.innobyte.blog_api.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostController(PostRepository postRepository,
                          UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody PostRequest request,
            Authentication authentication) {

        User author = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(author);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postRepository.save(post));
    }

    @GetMapping
    public Object getAll() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(p -> Map.of(
                        "id", p.getId(),
                        "title", p.getTitle(),
                        "content", p.getContent(),
                        "author", p.getAuthor().getUsername(),
                        "createdAt", p.getCreatedAt().toString()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return postRepository.findById(id)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(
                        Map.of(
                                "id", p.getId(),
                                "title", p.getTitle(),
                                "content", p.getContent(),
                                "author", p.getAuthor().getUsername(),
                                "createdAt", p.getCreatedAt().toString()
                        )))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Post not found")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest request,
            Authentication authentication) {

        Post post = postRepository.findById(id).orElse(null);

        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Post not found"));
        }

        if (!post.getAuthor().getUsername()
                .equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "You cannot edit this post"));
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        return ResponseEntity.ok(postRepository.save(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            Authentication authentication) {

        Post post = postRepository.findById(id).orElse(null);

        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Post not found"));
        }

        if (!post.getAuthor().getUsername()
                .equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "You cannot delete this post"));
        }

        postRepository.delete(post);
        return ResponseEntity.noContent().build();
    }
}