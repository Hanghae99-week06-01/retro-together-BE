package com.onetier.retro_together.controller;

import com.onetier.retro_together.controller.request.LikeRequestDto;
import com.onetier.retro_together.controller.response.ResponseDto;
import com.onetier.retro_together.service.PostCommentLikeService;
import com.onetier.retro_together.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * LikeController 2022-10-25 추가
 */
@Validated
@RequiredArgsConstructor
@RestController
public class LikeController {
    private final PostLikeService postLikeService;
    private final PostCommentLikeService postCommentLikeService;

    /**
     * 게시글 좋아요
     * @param requestDto
     * @param request
     * @return
     * @author doosan
     */
    @RequestMapping(value = "/api/auth/post/like", method = RequestMethod.POST)
    public ResponseDto<?> createPostLike(@RequestBody LikeRequestDto requestDto, HttpServletRequest request) {
        return postLikeService.createPostLike(requestDto, request);
    }

    /**
     * 댓글 좋아요
     * @param requestDto
     * @param request
     * @return
     * @author doosan
     */
    @RequestMapping(value = "/api/auth/postComment/like", method = RequestMethod.POST)
    public ResponseDto<?> createPostCommentLike(@RequestBody LikeRequestDto requestDto, HttpServletRequest request) {
        return postCommentLikeService.createPostCommentLike(requestDto , request);
    }
}