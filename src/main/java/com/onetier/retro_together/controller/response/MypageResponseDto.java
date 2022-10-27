package com.onetier.retro_together.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * MypageResponseDto 2022-10-27 mypage 추가
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MypageResponseDto {
    private List<PostResponseDto> mypagePosts;
    private List<CommentResponseDto> mypageComments;
    private List<PostResponseDto> likedPosts;
    private List<CommentResponseDto> likedComments;
}
