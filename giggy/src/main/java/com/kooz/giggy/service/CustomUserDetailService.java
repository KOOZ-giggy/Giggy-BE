package com.kooz.giggy.service;

import com.kooz.giggy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// TODO: 9/25/24 UserDetailService는 토큰의 인증 정보를 조회할 떄 사용.

//@Service
//@RequiredArgsConstructor
//public class UserDetailService implements UserDetailsService {
//    private final MemberRepository memberRepository;


//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findby
//    }
//}


@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByProviderId(username);
//        return memberRepository.findByProviderId(username)
//                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}