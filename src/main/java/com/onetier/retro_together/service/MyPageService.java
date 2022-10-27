package com.onetier.retro_together.service;

import com.onetier.retro_together.controller.response.CommentResponseDto;
import com.onetier.retro_together.controller.response.MypageResponseDto;
import com.onetier.retro_together.controller.response.PostResponseDto;
import com.onetier.retro_together.controller.response.ResponseDto;
import com.onetier.retro_together.domain.*;
import com.onetier.retro_together.jwt.TokenProvider;
import com.onetier.retro_together.repository.CommentRepository;
import com.onetier.retro_together.repository.PostCommentLikeRepository;
import com.onetier.retro_together.repository.PostLikeRepository;
import com.onetier.retro_together.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * MyPageService 2022-10-27 추가
 */
@RequiredArgsConstructor
@Service
public class MyPageService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final PostCommentLikeRepository postCommentLikeRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> getMyPage(HttpServletRequest request) {

        // 리프레시 토큰을 이용한 null 값 체크
        if(null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        // accessToken을 이용한 null 값 체크
        if(null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        // member null값 체크
        Member member = validateMember(request);
        if(null == member) {
            return ResponseDto.fail("INVALIED_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 내가 작성한 게시글
        List<Post> myPostList = postRepository.findAllByMember(member);
        List<PostResponseDto> myPostResponseDtoList = new ArrayList<>();

        for(Post post : myPostList) {
            myPostResponseDtoList.add(
                    PostResponseDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .author(post.getMember().getNickname())
                            .content(post.getContent())
                            .likeCount(postLikeRepository.countAllByPostId(post.getId()))
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .comment_cnt(post.getComment_cnt())
                            .build()
            );
        }

        // 내가 작성한 댓글, 대댓글
        List<Comment> myComentList = commentRepository.findAllByMember(member);
        List<CommentResponseDto> myCommentResponseDtoList = new ArrayList<>();

        for(Comment comment: myComentList) {
            myCommentResponseDtoList.add (
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .author(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .likeCount(postCommentLikeRepository.countAllByCommentId(comment.getId()))
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }

        // 내가 좋아요한 게시글
        List<PostLike> postLikeList = postLikeRepository.findAllByMember(member);
        List<PostResponseDto> LikedPostResponseDtoList = new ArrayList<>();

        for(PostLike postLike : postLikeList) {
            LikedPostResponseDtoList.add (
                    PostResponseDto.builder()
                            .id(postLike.getPost().getId())
                            .title(postLike.getPost().getTitle())
                            .author(postLike.getPost().getMember().getNickname())
                            .content(postLike.getPost().getContent())
                            .likeCount(postLikeRepository.countAllByPostId(postLike.getPost().getId()))
                            .createdAt(postLike.getPost().getCreatedAt())
                            .modifiedAt(postLike.getPost().getModifiedAt())
                            .comment_cnt(postLike.getPost().getComment_cnt())
                            .build()
            );
        }

        //내가 좋아요한 댓글
        List<PostCommentLike> postCommentLikeList = postCommentLikeRepository.findAllByMember(member);
        List<CommentResponseDto> likedCommentResponseDtoList = new ArrayList<>();

        for(PostCommentLike postCommentLike : postCommentLikeList) {
            likedCommentResponseDtoList.add (
                    CommentResponseDto.builder()
                            .id(postCommentLike.getComment().getId())
                            .author(postCommentLike.getComment().getMember().getNickname())
                            .content(postCommentLike.getComment().getContent())
                            .likeCount(postCommentLikeRepository.countAllByCommentId(postCommentLike.getComment().getId()))
                            .createdAt(postCommentLike.getComment().getCreatedAt())
                            .modifiedAt(postCommentLike.getComment().getModifiedAt())
                            .build()
            );
        }

        return ResponseDto.success(
                MypageResponseDto.builder()
                        .mypagePosts(myPostResponseDtoList)
                        .mypageComments(myCommentResponseDtoList)
                        .likedPosts(LikedPostResponseDtoList)
                        .likedComments(likedCommentResponseDtoList)
                        .build()
        );
    }

    /**
     * 멤버 유효성 체크
     * @param request
     * @return
     * @author doosan
     */
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if(!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}