package com.onetier.retro_togather.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id; // 아이디
    private String nickname;  // 닉네임
    private LocalDateTime createdAt; // 생성일
    private LocalDateTime modifiedAt; // 수정일
    private String email; //이메일

}
