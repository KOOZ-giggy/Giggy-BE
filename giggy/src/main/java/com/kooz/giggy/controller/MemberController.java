package com.kooz.giggy.controller;

import com.kooz.giggy.dto.jwt.JwtResponse;
import com.kooz.giggy.dto.oauth.GoogleOAuthRequest;
import com.kooz.giggy.dto.oauth.GoogleOAuthResponse;
import com.kooz.giggy.dto.oauth.GoogleUserProfileRequest;
import com.kooz.giggy.dto.oauth.GoogleUserProfileResponse;
import com.kooz.giggy.dto.sign.MemberResponse;
import com.kooz.giggy.dto.sign.SignUpRequest;
import com.kooz.giggy.dto.sign.SignUpResponse;
import com.kooz.giggy.entity.user.Member;
import com.kooz.giggy.entity.user.UserRole;
import com.kooz.giggy.service.GoogleOAuthClient;
import com.kooz.giggy.service.MemberService;
import com.kooz.giggy.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
@Tag(name = "Member", description = "멤버 도메인 관련 API")
public class MemberController {

    private final MemberService memberService;
    private final GoogleOAuthClient googleOAuthClient;

    @Autowired
    private final JwtUtil jwtUtil;

    // 회원가입
//    @PostMapping("/signup")
//    @Operation(summary = "회원가입")
//    @Parameters({
//            @Parameter(name = "providerType", description = "OAuth Provider(e.g. GOOGLE || APPLE)"),
//            @Parameter(name = "authCode", description = "Authentication Code")
//    })
//    public JwtResponse signup(SignUpRequest request) {
//        GoogleOAuthResponse oAuthResponse = googleOAuthClient.getTokens(new GoogleOAuthRequest(request.getAuthCode()));
//        GoogleUserProfileResponse userProfile = googleOAuthClient.getUserProfile(new GoogleUserProfileRequest(oAuthResponse.accessToken()));
//
//        String providerId = userProfile.getId();
//        String email = userProfile.getEmail();
//
//        Optional<Member> member = memberService.findByEmail(email);
//
//        if (member.isPresent()) {
//            // 이미 회원가입이 된 멤버의 경우
//            String accessToken = jwtUtil.generateAccessToken(providerId, UserRole.EMPLOYEE.getValue());
//            String refreshToken = jwtUtil.generateRefreshToken(providerId);
//            return new JwtResponse("Bearer", accessToken, refreshToken);
//        } else {
//            // 첫 회원가입의 경우
////            SignUpResponse signUpResponse = memberService.registerMember(request);
//            MemberResponse memberResponse = memberService.registerMember(request);
//            String accessToken = jwtUtil.generateAccessToken(providerId, UserRole.EMPLOYEE.getValue());
//            String refreshToken = jwtUtil.generateRefreshToken(providerId);
//            return new JwtResponse("Bearer", accessToken, refreshToken);
//        }
//    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    @Parameters({
            @Parameter(name = "providerType", description = "OAuth Provider(e.g. GOOGLE || APPLE)"),
            @Parameter(name = "authCode", description = "Authentication Code")
    })
    public SignUpResponse signup(SignUpRequest request) {
        GoogleOAuthResponse oAuthResponse = googleOAuthClient.getTokens(new GoogleOAuthRequest(request.getAuthCode()));
        GoogleUserProfileResponse userProfile = googleOAuthClient.getUserProfile(new GoogleUserProfileRequest(oAuthResponse.accessToken()));

        String providerId = userProfile.getId();
        String email = userProfile.getEmail();

        Optional<Member> member = memberService.findByEmail(email);

        if (member.isPresent()) {
            // 기가입의 경우
            MemberResponse memberResponse = MemberResponse.from(member.get());
            JwtResponse jwtResponse = new JwtResponse(
                    "Bearer",
                    jwtUtil.generateAccessToken(providerId, UserRole.EMPLOYEE.getValue()),
                    jwtUtil.generateRefreshToken(providerId)
            );
            return new SignUpResponse(jwtResponse, memberResponse);
        } else {
            // 첫 회원가입의 경우
            MemberResponse memberResponse = memberService.registerMember(request);
            JwtResponse jwtResponse = new JwtResponse(
                    "Bearer",
                    jwtUtil.generateAccessToken(providerId, UserRole.EMPLOYEE.getValue()),
                    jwtUtil.generateRefreshToken(providerId)
            );
            return new SignUpResponse(jwtResponse, memberResponse);
        }
    }
}
