package com.onetier.retro_togather.service;

import com.onetier.retro_togather.controller.request.LoginRequestDto;
import com.onetier.retro_togather.controller.request.MemberRequestDto;
import com.onetier.retro_togather.controller.request.TokenDto;
import com.onetier.retro_togather.controller.response.MemberResponseDto;
import com.onetier.retro_togather.controller.response.ResponseDto;
import com.onetier.retro_togather.domain.Member;
import com.onetier.retro_togather.jwt.TokenProvider;
import com.onetier.retro_togather.repository.MemberRepository;
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

    /**
     * 유효성검사
     * @param requestDto
     * @return
     * @author doosan
     */
    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        if (null != isPresentMember(requestDto.getEmail())) {
            return ResponseDto.fail("DUPLICATED_NICKNAME",
                    "중복된 이메일 입니다.");
        }

        Member member = Member.builder()
                .nickname(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .build();
        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .email(member.getEmail())
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
        Member member =  isPresentMember(requestDto.getEmail());
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
                        .email(member.getEmail())
                        .build()
        );
    }

    @Transactional
    public Member isPresentMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
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
}
