package com.ranafahad.unifriends.post;

import com.ranafahad.unifriends.league.LeagueType;
import com.ranafahad.unifriends.post.dto.PostResponse;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public Page<PostResponse> findByType(LeagueType type, Pageable pageable) {
        return postRepository.findByTypeOrderByCreatedAtDesc(type, pageable)
                .map(PostResponse::from);
    }

    @Transactional
    public Post createPost(String callerEmail, String title, String content, LeagueType type, List<String> tags) {
        User user = userService.findByEmail(callerEmail);

        if (user.getBannedAt() != null) {
            throw new IllegalStateException("Banned users cannot create posts");
        }

        validateTitle(title);

        // Check user has matching profile type
        ProfileType requiredProfile = type == LeagueType.ACADEMIC ? ProfileType.STUDENT : ProfileType.PERSONALITY;
        profileRepository.findByUserIdAndType(user.getId(), requiredProfile)
                .orElseThrow(() -> new IllegalStateException(
                        "You need a " + (requiredProfile == ProfileType.STUDENT ? "Student" : "Personality")
                        + " profile to post in the "
                        + (type == LeagueType.ACADEMIC ? "Academic" : "Extracurriculars")
                        + " feed"));

        String tagsValue = buildTags(tags);

        Post post = Post.builder()
                .author(user)
                .title(title.strip())
                .content(content)
                .type(type)
                .tags(tagsValue)
                .build();
        return postRepository.save(post);
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalStateException("Post title must not be blank");
        }
        if (title.strip().length() > 150) {
            throw new IllegalStateException("Post title must not exceed 150 characters");
        }
    }

    private String buildTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        if (tags.size() > 5) {
            throw new IllegalStateException("A post can have at most 5 tags");
        }
        for (String tag : tags) {
            if (tag == null || tag.isBlank()) {
                throw new IllegalStateException("Tags must not be blank");
            }
            if (tag.length() > 20) {
                throw new IllegalStateException("Each tag must not exceed 20 characters");
            }
            if (!tag.matches("^[a-z0-9-]+$")) {
                throw new IllegalStateException(
                        "Tags may only contain lowercase letters, numbers, and hyphens (got: \"" + tag + "\")");
            }
        }
        return String.join(",", tags);
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
