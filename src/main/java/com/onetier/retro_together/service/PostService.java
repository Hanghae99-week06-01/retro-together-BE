package com.onetier.retro_together.service;


import com.onetier.retro_together.controller.request.GetAllPostResponseDto;
import com.onetier.retro_together.controller.request.PostRequestDto;
import com.onetier.retro_together.controller.response.*;


import com.onetier.retro_together.domain.Member;
import com.onetier.retro_together.domain.Post;
import com.onetier.retro_together.jwt.TokenProvider;

import com.onetier.retro_together.repository.*;

import com.onetier.retro_together.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PostService 수정함 2022- 10 - 23 오후 7시 34분
 */
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TokenProvider tokenProvider;
    private final S3UploaderService s3UploaderService; // S3Uploader 관련 추가 2022- 10 - 23 오후 7시 34분

    private final ImageRepository imageRepository; // ImageRepository 관련 추가 2022-10-23 오후 7시 54분

    /**
     * 게시글 등록
     *
     * @param requestDto
     * @param multipartFile
     * @param request
     * @return
     */
    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto, MultipartFile multipartFile, HttpServletRequest request) {

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


        // AWS 추가 2022-10-23 오후 8시 8분
        String FileName = null;
        if (multipartFile.isEmpty()) {
            return ResponseDto.fail("INVALID_FILE", "파일이 유효하지 않습니다.");
        }
        ImageResponseDto imageResponseDto = null;
        try {
            FileName = s3UploaderService.uploadFile(multipartFile, "image");
            imageResponseDto = new ImageResponseDto(FileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert imageResponseDto != null;
        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .image(imageResponseDto.getImageUrl())
                .member(member)
                .build();
        postRepository.save(post);

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .author(post.getMember().getNickname())
                        .imageUrl(post.getImage())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

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
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(getAllPostResponseDtoList);
    }

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

        assert imageResponseDto != null;
        post.update(requestDto, imageResponseDto);

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .imageUrl(post.getImage())
                        .author(post.getMember().getNickname())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

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


    @Transactional(readOnly = true)
    public ResponseDto<?> getPost(Long id) {
        Post post = isPresentPost(id);
        if (null == post) {

        }
        return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }
}
