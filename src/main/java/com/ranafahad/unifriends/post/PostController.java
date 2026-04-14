package com.ranafahad.unifriends.post;

import com.ranafahad.unifriends.league.LeagueType;
import com.ranafahad.unifriends.post.dto.CreatePostRequest;
import com.ranafahad.unifriends.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getPosts(
            @RequestParam LeagueType type,
            @RequestParam(defaultValue = "0") int page) {
        Page<Post> posts = postService.findByType(type, PageRequest.of(page, 20));
        return ResponseEntity.ok(posts.map(PostResponse::from));
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestBody CreatePostRequest request,
            Principal principal) {
        Post post = postService.createPost(principal.getName(), request.title(), request.content(), request.type(), request.tags());
        return ResponseEntity.ok(PostResponse.from(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, Principal principal) {
        postService.deletePost(id, principal.getName());
        return ResponseEntity.ok().build();
    }
}
