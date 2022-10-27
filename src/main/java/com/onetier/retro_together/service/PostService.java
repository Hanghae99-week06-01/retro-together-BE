package com.onetier.retro_together.service;


import com.onetier.retro_together.controller.request.PostRequestDto;
import com.onetier.retro_together.controller.response.*;
import com.onetier.retro_together.domain.*;
import com.onetier.retro_together.domain.PostTag;
import com.onetier.retro_together.jwt.TokenProvider;

import com.onetier.retro_together.repository.*;

import com.onetier.retro_together.repository.PostRepository;
import com.onetier.retro_together.util.ClientUtil;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

/**
 * PostService 수정함 2022- 10 - 23 오후 7시 34분
 */
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final TokenProvider tokenProvider;
    private final S3UploaderService s3UploaderService; // S3Uploader 관련 추가 2022- 10 - 23 오후 7시 34분

    private final ImageRepository imageRepository; // ImageRepository 관련 추가 2022-10-23 오후 7시 54분

    private final CommentRepository commentRepository;

    private final PostLikeRepository postLikeRepository;

    private final PostCommentLikeRepository postCommentLikeRepository;

    private final ClientUtil clientUtil;


    /**
     * 게시글 등록
     *
     * @param postRequestDto
     * @param request
     * @return
     * @author doosan
     */
    @Transactional
    public ResponseDto<?> createPost(PostRequestDto postRequestDto, HttpServletRequest request) throws Exception {

        /** Refresh-Token 유효성 검사 */
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        /** Authorization 유효성 검사 */
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        /** member 유효성 검사 */
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }


//        // AWS 추가 2022-10-23 오후 8시 8분
//        String FileName = null;
//        if (multipartFile.isEmpty()) {
//            return ResponseDto.fail("INVALID_FILE", "파일이 유효하지 않습니다.");
//        }
//        ImageResponseDto imageResponseDto = null;
//        try {
//            FileName = s3UploaderService.uploadFile(multipartFile, "image");
//            imageResponseDto = new ImageResponseDto(FileName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        List<Tag> inputTag = Arrays.stream(postRequestDto.getTags().split(" ")).map( // 2022-10-23 오후 8시 8분
                tag -> tagRepository.findByTagName(tag).orElseGet(() -> tagRepository.save(new Tag(tag)))
        ).toList();


//        assert imageResponseDto != null;
        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .image(postRequestDto.getImageUrl())
                .comment_cnt(0)        // 게시글 카운트 추가
              //  .image(imageResponseDto.getImageUrl())      // ImageUrl 추가
                .postTagList(inputTag.stream().map(tag -> PostTag.builder().tag(tag).build()).collect(Collectors.toList()))
                .member(member)
                .build();
        postRepository.save(post);
        clientUtil.requestgit(post.getTitle(), post.getContent());




        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .author(post.getMember().getNickname())
                        .imageUrl(post.getImage())
                        .category(post.getCategory().getValue())
                        .tags(post.getPostTagList().stream().map(postTag -> postTag.getTag().getTagName()).collect(Collectors.toList()))
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .comment_cnt(post.getComment_cnt()) // comment_cnt (게시글 카운트 항목) 추가
                        .likeCount(post.getLikeCount()) //  likeCount (좋아요 카운트 항목) 추가
                        .build()
        );
    }

    /**
     * 게시글 전체 조회
     * @param id
     * @return
     * @author doosan
     */
    @Transactional(readOnly = true)
    public ResponseDto<?> getPost(Long id) {
        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다.");
        }

        List<Comment> commentList = commentRepository.findAllByPostAndParent(post, null);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .author(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .likeCount(postCommentLikeRepository.countAllByCommentId(comment.getId()))
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .replies(replyListExtractor(post, comment))
                            .build()
            );
        }
        Long likeCount = postLikeRepository.countAllByPostId(post.getId()); //likeCount 추가

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .imageUrl(post.getImage())
                        .comment_cnt(post.getComment_cnt())
                        .author(post.getMember().getNickname())
                        .likeCount(likeCount)
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .comments(commentResponseDtoList)
                        .build()
        );
    }

    /**
     * replyListExtroctor
     * @param post
     * @param parent_comment
     * @return
     * @author doosan
     */
    public List<ReplyResponseDto> replyListExtractor(Post post , Comment parent_comment) {
        List<Comment> replyList = commentRepository.findAllByPostAndParent(post, parent_comment);
        List<ReplyResponseDto> replyResponseDtoList = new ArrayList<>();

        for(Comment reply : replyList) {
            replyResponseDtoList.add(
                    ReplyResponseDto.builder()
                            .id(reply.getId())
                            .parentId(parent_comment.getId())
                            .author(reply.getMember().getNickname())
                            .content(reply.getContent())
                            .likeCount(postCommentLikeRepository.countAllByCommentId(reply.getId()))
                            .createdAt(reply.getCreatedAt())
                            .modifiedAt(reply.getModifiedAt())
                            .build()
                            );
        }
        return replyResponseDtoList;
    }

    /**
     * 전체 게시글 조회
     * @return
     * @author doosan
     */
    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPost() {

        List<Post> allPosts = postRepository.findAllByOrderByModifiedAtDesc();
        List<GetAllPostResponseDto> getAllPostResponseDtoList = new ArrayList<>();

        for (Post post : allPosts) {
            getAllPostResponseDtoList.add(
                    GetAllPostResponseDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .author(post.getMember().getNickname())
                            .likeCount(postLikeRepository.countAllByPostId(post.getId())) // likeCount 추가
                            .comment_cnt(post.getComment_cnt()) // comment_cnt 추가
                            .tags(post.getPostTagList().stream().map(postTag -> postTag.getTag().getTagName()).collect(Collectors.toList()))
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(getAllPostResponseDtoList);
    }

    /**
     * 게시글 수정
     * @param id
     * @param requestDto
     * @param multipartFile
     * @param request
     * @return
     * @author doosan
     */
    @Transactional
    public ResponseDto<?> updatePost(Long id, PostRequestDto requestDto, MultipartFile multipartFile, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("Member_NOT_FOUND",
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

        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        //AWS
        String FileName = null;
        if (multipartFile.isEmpty()) {
            return ResponseDto.fail("INVALID_FILE", "파일이 유효하지 않습니다.");
        }

        ImageResponseDto imageResponseDto = null;
        if (!multipartFile.isEmpty()) {
            try {
                FileName = s3UploaderService.uploadFile(multipartFile, "image");
                imageResponseDto = new ImageResponseDto(FileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Arrays.stream(requestDto.getTags().split(" "))
                .map(tag -> tagRepository.findByTagName(tag).orElseGet(() -> tagRepository.save(new Tag(tag))))
                .forEach(tag -> {
                    if (!post.getPostTagList().stream().map(postTag -> postTag.getTag().getTagName()).toList().contains(tag.getTagName())) {
                        post.addPostTag(tag);
                    }
                });

        assert imageResponseDto != null;
        post.update(requestDto, imageResponseDto);

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .imageUrl(post.getImage())
                        .category(post.getCategory().getValue())
                        .author(post.getMember().getNickname())
                        .tags(post.getPostTagList().stream().map(postTag -> postTag.getTag().getTagName()).collect(Collectors.toList()))
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .likeCount(postLikeRepository.countAllByPostId(post.getId())) // likeCount
                        .comment_cnt(post.getComment_cnt()) // 게시글 카운트 추가
                        .build()
        );
    }

    /**
     * 게시글 삭제
     * @param id
     * @param request
     * @return
     * @author doosan
     */
    @Transactional
    public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {
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
            return ResponseDto.fail("INVALIED_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
        return ResponseDto.success("delete success");
    }

    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    // postService 수정 2022-10-23 오후 8시 19분

    /**
     * Refresh-Token 유효성 검사
     *
     * @param request
     * @return
     */
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
