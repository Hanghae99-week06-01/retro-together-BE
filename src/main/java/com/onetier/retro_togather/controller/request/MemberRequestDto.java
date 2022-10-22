package com.onetier.retro_togather.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * MemberRequestDto
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

    @NotBlank(message = "공백을 허용하지않습니다")
    @Size(min=2,max=20, message= "올바른 이메일형식이 아닙니다")
    private String email; // email

    @NotBlank(message = "공백을 허용하지 않습니다")
    @Size(min=4,max=12, message= "4자 이상 12자이하로 입력해주세요")
    private String nickname; // nickname

    @NotBlank(message = "공백을 허용하지않습니다")
    @Size(min=8,max=20, message= "8자 이상 20자이하로 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$"
            , message = "비밀번호가 영어대소문자, 숫자, 특수문자를 모두 포함해주세요")
    private String password; // password

    @NotBlank
    public String passwordConfirm;
}
