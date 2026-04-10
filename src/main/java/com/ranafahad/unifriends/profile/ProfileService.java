package com.ranafahad.unifriends.profile;

import com.ranafahad.unifriends.user.User;
import com.ranafahad.unifriends.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;

    public Profile findById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
    }

    public List<Profile> findByUser(Long userId) {
        return profileRepository.findByUserId(userId);
    }

    @Transactional
    public Profile updateProfile(Long profileId, String callerEmail, String bio, String tags, String hobbies) {
        Profile profile = findById(profileId);
        User caller = userService.findByEmail(callerEmail);

        if (!profile.getUser().getId().equals(caller.getId())) {
            throw new AccessDeniedException("You can only update your own profile");
        }

        profile.setBio(bio);
        profile.setTags(tags);
        profile.setHobbies(hobbies);
        return profileRepository.save(profile);
    }
}
