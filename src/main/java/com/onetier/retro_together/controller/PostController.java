package com.onetier.retro_together.controller;

import com.onetier.retro_together.controller.request.PostRequestDto;
import com.onetier.retro_together.controller.response.ResponseDto;
import com.onetier.retro_together.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * PostController createPost부분에 multipartFile 추가 2022-10-23 오후 7시 15분
 */
@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    /**
     * 게시글 등록 , POST 요청
     * @param requestDto
     * @param multipartFile
     * @param request
     * @return
     * @author doosan
     */
    @RequestMapping(value="/api/auth/post", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
    public ResponseDto<?> createPost(@RequestPart(value = "requestDto") PostRequestDto requestDto,
                                     @RequestPart(value = "file", required = false) MultipartFile multipartFile,
                                     HttpServletRequest request) {
        return postService.createPost(requestDto, multipartFile, request);
    }

    /**
     * 단건 게시글 조회, GET 요청
     * @param id
     * @return
     * @author doosan
     */
    @RequestMapping(value = "/api/post/{id}", method = RequestMethod.GET)
    public ResponseDto<?> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    /**
     * 전체 게시글 조회, GET 요청
     * @return
     * @author doosan
     */
    @RequestMapping(value = "/api/post", method = RequestMethod.GET)
    public ResponseDto<?> getAllPosts() {
        return postService.getAllPost();
    }

    /**
     * 게시글 수정 , PUT 요청
     * @param id
     * @param requestDto
     * @param multipartFile
     * @param request
     * @return
     * @author doosan
     */
    @RequestMapping(value = "/api/auth/post/{id}" , method = RequestMethod.PUT)
    public ResponseDto<?> updatePost(@PathVariable Long id, @RequestPart PostRequestDto requestDto, @RequestPart MultipartFile multipartFile, HttpServletRequest request) {
        return postService.updatePost(id, requestDto, multipartFile, request);   // 수정 2022-10-23 오후 7시 30분
    }

    /**
     * 게시글 삭제, DELETE 요청
     * @param id
     * @param request
     * @return
     * @author doosan
     */
    @RequestMapping(value = "/api/auth/post/{id}", method = RequestMethod.DELETE)
    public ResponseDto<?> deletePost(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id,request);
    }
}