package com.onetier.retro_together.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * GetAllPostResponseDto 추가 2022 - 10 - 23 오후 8시 30분
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllPostResponseDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
