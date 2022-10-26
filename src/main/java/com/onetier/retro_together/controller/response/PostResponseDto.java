package com.onetier.retro_together.controller.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * PostResponseDto 2022-10-23 오후 7시 11분 imageUrl , author 추가 post dev push 하고 나중에 comment 추가 예정
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String author;
    private String content;
    private String title;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer comment_cnt; // 2022-10-24 오후 5시 29분 추가
    private List<CommentResponseDto> comments; // 2022-10-24 오후 5시 29분 추가
    private Long likeCount; // 2022-10-25 좋아요 카운트 추가
}
