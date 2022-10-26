package com.onetier.retro_together.service;

import com.onetier.retro_together.controller.request.LikeRequestDto;
import com.onetier.retro_together.controller.response.ResponseDto;
import com.onetier.retro_together.domain.Member;
import com.onetier.retro_together.domain.Post;
import com.onetier.retro_together.domain.PostLike;
import com.onetier.retro_together.jwt.TokenProvider;
import com.onetier.retro_together.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
/**
 * PostLikeService 추가 2022-10-25
 */
@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostService postService;

    private final PostLikeRepository postLikeRepository;

    private final TokenProvider tokenProvider;

    /**
     * 게시글 좋아요 등록
     * @param requestDto
     * @param request
     * @return
     * @author doosan
     */
    @Transactional
    public ResponseDto<?> createPostLike(LikeRequestDto requestDto, HttpServletRequest request) {

        // 리프레시토큰으로 유효성 검사
        if(null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        // 엑세스 토큰으로 유효성 검사
        if(null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        // 멤버 null값 유효성 검사
        Member member = validateMember(request);
        if(null == member) {
            return ResponseDto.fail("INVALID_TOKEN" , "Token이 유효하지 않습니다.");
        }

        // 포스트 null값 유효성 검사
        Post post = postService.isPresentPost(requestDto.getId());
        if(null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다.");
        }

        // 게시글 좋아요 취소
        PostLike findPostLike = postLikeRepository.findByPostIdAndMemberId(post.getId(), member.getId());
        if(null != findPostLike) {
            postLikeRepository.delete(findPostLike);
            return ResponseDto.success("좋아요 취소");
        }

        PostLike postlike = PostLike.builder()
                .member(member)
                .post(post)
                .build();
        postLikeRepository.save(postlike);
        return ResponseDto.success("게시글 좋아요 완료");

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
