package com.onetier.retro_together.service;

import com.onetier.retro_together.controller.request.LoginRequestDto;
import com.onetier.retro_together.controller.request.MemberRequestDto;
import com.onetier.retro_together.controller.request.TokenDto;
import com.onetier.retro_together.controller.response.MemberResponseDto;
import com.onetier.retro_together.controller.response.ResponseDto;
import com.onetier.retro_together.domain.Member;
import com.onetier.retro_together.jwt.TokenProvider;
import com.onetier.retro_together.repository.MemberRepository;
import com.onetier.retro_together.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 유효성검사
     * @param requestDto
     * @return
     * @author doosan
     */
    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        if (null != isPresentMember(requestDto.getEmailId())) {
            return ResponseDto.fail("DUPLICATED_EMAILID",
                    "중복된 이메일 입니다.");
        }

        Member member = Member.builder()
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .emailId(requestDto.getEmailId())
                .build();
        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .emailId(member.getEmailId())
                        .build()
        );
    }

    /**
     * 로그인
     * @param requestDto
     * @param response
     * @return
     * @author doosan
     */
    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member =  isPresentMember(requestDto.getEmailId());
        if(null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        if(!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다.");

        }
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .emailId(member.getEmailId())
                        .build()
        );
    }

    @Transactional
    public Member isPresentMember(String emailId) {
        Optional<Member> optionalMember = memberRepository.findByEmailId(emailId);
        return optionalMember.orElse(null);
    }

    /**
     * 로그아웃
     * @param request
     * @return
     * @author doosan
     */
    public ResponseDto<?> logout (HttpServletRequest request) {
        if(!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        Member member = (Member) tokenProvider.getMemberFromAuthentication();
        if(null == member ){
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");

        }
        return tokenProvider.deleteRefreshToken(member);
    }

    /**
     * 토큰헤더
     * @param tokenDto
     * @param response
     * @author doosan
     */
    private void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

    /**
     * 토큰 재발급
     * @param request
     * @param response
     * @author Puri12
     */
    public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        if(!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Member member = refreshTokenRepository.findByValue(request.getHeader("Refresh-Token")).get().getMember();
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }
}
