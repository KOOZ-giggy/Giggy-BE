package com.kooz.giggy.service;

import com.kooz.giggy.dto.sign.SignUpRequest;
import com.kooz.giggy.dto.sign.SignUpResponse;
import com.kooz.giggy.entity.user.OAuthProviderType;
import com.kooz.giggy.entity.user.Member;
import com.kooz.giggy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Optional<Member> findByGoogleProviderId(String providerId) {
        return memberRepository.findByProviderId(providerId);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member saveUser(OAuthProviderType provider, String providerId, String email) {
        Member user = Member.builder()
                .provider(provider)
                .providerId(providerId)
                .email(email)
                .build();

        return memberRepository.save(user);
    }

    @Transactional
    public SignUpResponse registerMember(SignUpRequest request) {
        Member member = memberRepository.save(Member.from(request, new BCryptPasswordEncoder()));

        try {
            memberRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 사용중인 ID입니다.");
        }
        return SignUpResponse.from(member);
    }
}
