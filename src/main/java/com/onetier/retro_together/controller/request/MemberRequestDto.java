package com.onetier.retro_together.controller.request;


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

    @NotBlank(message = "공백은 허용하지 않습니다.")
    @Size(min=8,max=30, message= "8자리이상 30자리 미만 글자로 emailId를 만들어주세요")
    @Pattern(regexp = "^[0-9a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]+$" , message = "이메일 형식을 확인해 주세요.")
    private String emailId; // emailId

    @NotBlank(message = "닉네임에 공백은 허용되지 않습니다.")
    @Size(min=4,max=12, message= "닉네임은 최소 4자이상 최대 12자미만으로 만들어주세요.")
    @Pattern(regexp = "[a-zA-Z\\d]*${3,12}", message = "닉네임 형식을 확인해 주세요.")
    private String nickname; // nickname

    @NotBlank(message = "비밀번호에 공백은 허용되지 않습니다.")
    @Size(min=4,max=32, message= "비밀번호는 최소 4자이상 최대 32자미만으로 만들어주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$"
            , message = "비밀번호에 영어대소문자, 숫자, 특수문자를 모두 포함해주세요")
    private String password; // password

    @NotBlank
    public String passwordConfirm;
}
