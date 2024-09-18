package com.kooz.giggy.domain.user.service;

import com.kooz.giggy.domain.user.dto.UserUpdateRequest;
import com.kooz.giggy.domain.user.entity.OAuthProviderType;
import com.kooz.giggy.domain.user.entity.User;
import com.kooz.giggy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private final PasswordEncoder encoder;

    public Optional<User> findByGoogleProviderId(String providerId) {
        return userRepository.findByProviderId(providerId);
    }

    public User saveUser(OAuthProviderType provider, String providerId) {
        User user = User.builder()
                .provider(provider)
                .providerId(providerId)
                .build();

        return userRepository.save(user);
    }
}
