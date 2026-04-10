package com.ranafahad.unifriends.post;

import com.ranafahad.unifriends.league.LeagueType;
import com.ranafahad.unifriends.profile.ProfileRepository;
import com.ranafahad.unifriends.profile.ProfileType;
import com.ranafahad.unifriends.user.User;
import com.ranafahad.unifriends.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ProfileRepository profileRepository;

    public Page<Post> findByType(LeagueType type, Pageable pageable) {
        return postRepository.findByTypeOrderByCreatedAtDesc(type, pageable);
    }

    @Transactional
    public Post createPost(String callerEmail, String content, LeagueType type) {
        User user = userService.findByEmail(callerEmail);

        if (user.getBannedAt() != null) {
            throw new IllegalStateException("Banned users cannot create posts");
        }

        // Check user has matching profile type
        ProfileType requiredProfile = type == LeagueType.ACADEMIC ? ProfileType.STUDENT : ProfileType.PERSONALITY;
        profileRepository.findByUserIdAndType(user.getId(), requiredProfile)
                .orElseThrow(() -> new IllegalStateException(
                        "You need a " + requiredProfile + " profile to post in " + type + " feed"));

        Post post = Post.builder()
                .author(user)
                .content(content)
                .type(type)
                .build();
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId, String callerEmail) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        User caller = userService.findByEmail(callerEmail);

        if (!post.getAuthor().getId().equals(caller.getId())) {
            throw new AccessDeniedException("You can only delete your own posts");
        }
        postRepository.delete(post);
    }
}
