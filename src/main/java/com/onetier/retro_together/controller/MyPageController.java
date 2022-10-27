package com.onetier.retro_together.controller;

import com.onetier.retro_together.controller.response.ResponseDto;
import com.onetier.retro_together.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController
public class MyPageController {
    private final MyPageService myPageService;

    @RequestMapping(value = "/api/auth/mypage", method = RequestMethod.GET)
    public ResponseDto<?> getMyPage(HttpServletRequest request) {
        return myPageService.getMyPage(request);
    }
}
