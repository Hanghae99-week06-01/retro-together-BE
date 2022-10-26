package com.onetier.retro_together.service;

import com.onetier.retro_together.controller.request.CommentRequestDto;
import com.onetier.retro_together.controller.response.CommentResponseDto;
import com.onetier.retro_together.controller.response.ReplyResponseDto;
import com.onetier.retro_together.controller.response.ResponseDto;
import com.onetier.retro_together.domain.Comment;
import com.onetier.retro_together.domain.Member;
import com.onetier.retro_together.domain.Post;
import com.onetier.retro_together.jwt.TokenProvider;
import com.onetier.retro_together.repository.CommentRepository;
import com.onetier.retro_together.repository.PostCommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * CommentService 2022-10-24
 */
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostCommentLikeRepository postCommentLikeRepository;
    private final PostService postService;
    private final TokenProvider tokenProvider;

    // PostCommentLikeRepository  추가  2022-10-25

    /**
     * 게시글 등록
     * @param requestDto
     * @param request
     * @return
     * @author doosan
     */
    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto, HttpServletRequest request) {
        if(null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if(null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if(null == member){
            return ResponseDto.fail("INVALID_TOKEN","Token이 유효하지 않습니다.");
        }

        Post post = postService.isPresentPost(requestDto.getPostId());
        if(null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다.");
        }

        // 댓글 수 추가 2022-10-24 오후 2시 25분
        post.comment_cnt_Up();

        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);

        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .author(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .likeCount(comment.getLikeCount())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> createReply(Long parent_id, CommentRequestDto requestDto, HttpServletRequest request) {
        if(null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if(null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if(null == member) {
            return ResponseDto.fail("INVALIED_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = postService.isPresentPost(requestDto.getPostId());
        if(null ==post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다.");
        }

        Comment parent = hasParentComment(parent_id);
        if(parent.getParent() !=null) {
            return ResponseDto.fail("BAD_REQUEST",
                    "대댓글에는 댓글을 달 수 없습니다.");
        }

        Comment reply = Comment.builder()
                .member(member)
                .post(post)
                .content(requestDto.getContent())
                .parent(parent)
                .replies(commentRepository.findAllByPostAndParent(post, parent))
                .build();
        commentRepository.save(reply);

        return ResponseDto.success(
                ReplyResponseDto.builder()
                        .id(reply.getId())
                        .parentId(reply.getParent().getId())
                        .author(reply.getMember().getNickname())
                        .content(reply.getContent())
                        .createdAt(reply.getCreatedAt())
                        .modifiedAt(reply.getModifiedAt())
                        .likeCount(reply.getLikeCount())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllCommentsByPost(Long postId) {
        Post post = postService.isPresentPost(postId);
        if(null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다.");
        }

        List<Comment> commentList = commentRepository.findAllByPostAndParent(post, null);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for(Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .author(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .replies(replyListExtractor(post,comment))
                            .likeCount(postCommentLikeRepository.countAllByCommentId(comment.getId()))
                            .build()
            );
        }
        return ResponseDto.success(commentResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> updateComment(Long comment_id, CommentRequestDto requestDto, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = postService.isPresentPost(requestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", " 존재하지 않는 게시글 ID 입니다.");
        }

        Comment comment = isPresentComment(comment_id);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 ID 입니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        comment.update(requestDto);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .author(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .replies(replyListExtractor(post, comment))
                        .likeCount(postCommentLikeRepository.countAllByCommentId(comment.getId()))
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deleteComment(Long comment_id, HttpServletRequest request) {
        if(null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.") ;
        }

        if(null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if(null == member) {
            return ResponseDto.fail("INVALID_TOKEN","Token이 유효하지 않습니다.");
        }

        Comment comment= isPresentComment(comment_id);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", " 존재하지 않는 댓글 ID 입니다.");
        }

        if(comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        Post post = comment.getPost();
        if(comment.getPost() == null) {
            post.comment_cnt_Down();
        }

        commentRepository.delete(comment);

        return ResponseDto.success("success");
    }

    @Transactional(readOnly = true)
    public Comment isPresentComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }

    @Transactional(readOnly = true)
    public Comment hasParentComment(Long parent_id) {
        Optional<Comment> parent = commentRepository.findById(parent_id);
        return parent.orElse(null);
    }

    @Transactional
    public List<ReplyResponseDto> replyListExtractor(Post post , Comment parent_comment) {

        List<Comment> replyList = commentRepository.findAllByPostAndParent(post, parent_comment);
        List<ReplyResponseDto> replyResponseDtoList = new ArrayList<>();

        for (Comment reply : replyList) {
            replyResponseDtoList.add(
                    ReplyResponseDto.builder()
                            .id(reply.getId())
                            .parentId(parent_comment.getId())
                            .author(reply.getMember().getNickname())
                            .content(reply.getContent())
                            .createdAt(reply.getCreatedAt())
                            .modifiedAt(reply.getModifiedAt())
                            .build()
            );
        }
        return replyResponseDtoList;
    }

    /**
     *  Refresh-Token으로 member 유효성 검사
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
