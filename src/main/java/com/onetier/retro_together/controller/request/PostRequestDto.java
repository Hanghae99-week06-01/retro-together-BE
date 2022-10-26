package com.onetier.retro_together.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PostRequestDto 2022-10-23 오후 7시 10분 imageUrl 추가
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    private String title;
    private String content;
    private String imageUrl;
    private String tags;
}
