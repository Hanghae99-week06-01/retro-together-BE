package com.onetier.retro_together.controller;

import com.onetier.retro_together.controller.request.LoginRequestDto;
import com.onetier.retro_together.controller.request.MemberRequestDto;
import com.onetier.retro_together.controller.response.ResponseDto;
import com.onetier.retro_together.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * MemberController
 */
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    /**
     * 회원가입
     * @param requestDto
     * @return
     * @author doosan
     */
    @PostMapping(value="/api/member/signup")
    public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
        return memberService.createMember(requestDto);
    }

    /**
     * 로그인
     * @param requestDto
     * @param response
     * @return
     * @author doosan
     */
    @PostMapping(value = "/api/member/login")
    public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response) {
        return memberService.login(requestDto, response);
    }

    /**
     * 로그아웃
     * @param request
     * @return
     * @author doosan
     */
    @PostMapping(value = "/api/auth/member/logout")
    public ResponseDto<?> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }

    @Transactional
    public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        return memberService.reissue(request, response);
    }
}
