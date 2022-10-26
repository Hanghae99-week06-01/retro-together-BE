package com.onetier.retro_together.service;

import com.onetier.retro_together.controller.request.LikeRequestDto;
import com.onetier.retro_together.controller.response.ResponseDto;
import com.onetier.retro_together.domain.Comment;
import com.onetier.retro_together.domain.Member;
import com.onetier.retro_together.domain.PostCommentLike;
import com.onetier.retro_together.jwt.TokenProvider;
import com.onetier.retro_together.repository.PostCommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class PostCommentLikeService {

    private final CommentService commentService;
    private final PostCommentLikeRepository postCommentLikeRepository;
    private final TokenProvider tokenProvider;

    /**
     * 댓글 좋아요 등록
     * @param requestDto
     * @param request
     * @return
     * @author doosan
     */
    @Transactional
    public ResponseDto<?> createPostCommentLike(LikeRequestDto requestDto, HttpServletRequest request) {
        // 리프레시 토큰으로 멤버 유효성 검사
        if(null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        // access 토큰으로 멤버 유효성 검사
        if(null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",  "로그인이 필요합니다.");
        }

        // 멤버 null값 유효성 검사
        Member member = validateMember(request);
        if(null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");

        }

        // 댓글 null값 유효성 검사
        Comment comment = commentService.isPresentComment(requestDto.getId());
        if(null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 ID 입니다.");
        }

        PostCommentLike checkLike = postCommentLikeRepository.findByCommentIdAndMemberId(comment.getId(), member.getId());


         // 좋아요 취소
        if ( null != checkLike)  {
            postCommentLikeRepository.delete(checkLike);
            return ResponseDto.success("좋아요 취소");
        }

        PostCommentLike postcommentLike = PostCommentLike.builder()
                .member(member)
                .comment(comment)
                .build();
        postCommentLikeRepository.save(postcommentLike);

        return ResponseDto.success(":댓글 좋아요 완료");
    }

    // 멤버 유효성 검사
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if(!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
