package com.onetier.retro_together.controller.request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * CommentRequestDto 2022-10-24 오후 1시 56분 추가 parentId 추가
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    private Long postId;
    private Long parentId;
    private String content;
}
