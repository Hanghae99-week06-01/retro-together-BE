package com.onetier.retro_together.controller.response;
import com.onetier.retro_together.domain.Post_Tag;
import com.onetier.retro_together.domain.Tag;
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
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
