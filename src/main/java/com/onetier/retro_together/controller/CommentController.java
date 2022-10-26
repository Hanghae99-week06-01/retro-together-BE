package com.onetier.retro_together.controller;

import com.onetier.retro_together.controller.request.CommentRequestDto;
import com.onetier.retro_together.controller.response.ResponseDto;
import com.onetier.retro_together.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * CommentController 오후 1시 58분 추가
 */
@Validated
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    /**
     * 댓글 등록
     * @param requestDto
     * @param request
     * @return
     * @author doosan
     */
    @RequestMapping(value="/api/auth/comment", method = RequestMethod.POST)
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(requestDto, request);
    }

    /**
     * 대댓글 등록
     * @param comment_id
     * @param requestDto
     * @param request
     * @return
     * @author doosan
     */
    @RequestMapping(value="/api/auth/comment/{comment_id}", method= RequestMethod.POST)
    public ResponseDto<?> createReply(@PathVariable Long comment_id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createReply(comment_id, requestDto, request);
    }

    /**
     * 댓글 전체 조회
     * @param post_id
     * @return
     * @author doosan
     */
    @RequestMapping(value="/api/comment/{post_id}", method = RequestMethod.GET)
    public ResponseDto<?> getAllComments(@PathVariable Long post_id) {
        return commentService.getAllCommentsByPost(post_id);
    }

    /**
     * 댓글 수정
     * @param comment_id
     * @param requestDto
     * @param request
     * @return
     * @author doosan
     */
    @RequestMapping(value="/api/auth/comment/{comment_id}", method=RequestMethod.PUT)
    public ResponseDto<?> updateComment(@PathVariable Long comment_id,
                                        @RequestBody CommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return commentService.updateComment(comment_id, requestDto, request);
    }

    /**
     * 댓글 삭제
     * @param comment_id
     * @param request
     * @return
     * @author doosan
     */
    @RequestMapping(value="/api/auth/comment/{comment_id}" , method= RequestMethod.DELETE)
    public ResponseDto<?> deleteComment(@PathVariable Long comment_id, HttpServletRequest request) {
        return commentService.deleteComment(comment_id, request);
    }
}