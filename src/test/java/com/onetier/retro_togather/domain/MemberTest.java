package com.onetier.retro_togather.domain;

import com.onetier.retro_together.controller.request.MemberRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    Long id = 100L; // 0L 일경우 테스트 오류
    String nickname = "doosan";
    String password = "1";
    String passwordConfirm = "1q2w3e4r!!";
    String emailId = "doosan0425@gmail.com";
    MemberRequestDto requestDto = new MemberRequestDto(
            nickname,
            password,
            passwordConfirm,
            emailId
    );

    @Test
    @DisplayName("정상 케이스")
    void testEquals() {
    }

    @Test
    void isPresentMember() {
//        if(requestDto.getEmail() == )
    }
    @Test
    void validatePassword() {
        if(id == null || id <=0) {
            throw new IllegalArgumentException("입력한 사용자 정보가 잘못되었습니다. ");
        }
    }

    @Test
    void getId() {
        if(requestDto.getEmailId() == null || requestDto.getEmailId().isEmpty()){
            throw new IllegalArgumentException("입력한 이메일이 잘못되었습니다. 정확한 이메일을 사용해주세요.");
        }
    }

    @Test
    void getPassword() {

            if(requestDto.getPassword() == null || requestDto.getPassword().isEmpty()){
                throw new IllegalArgumentException("입력한 패스워드가 잘못되었습니다. 정확한 패스워드를 입력해주세요.");
            }
            if(requestDto.getPassword().isBlank() || requestDto.getPasswordConfirm().isBlank()) {
                throw new IllegalArgumentException("패스워드에 공백은 인정되지 않습니다. 패스워드를 확인해주세요.");
            }
        }


    @Test
    void getNickname() {
        if(requestDto.getNickname() == null || requestDto.getNickname().isEmpty()){
            throw new IllegalArgumentException("저장할 수 있는 ID가 없습니다.");
        }
    }

    @Test
    void getEmailId() {
        if(requestDto.getEmailId() == null || requestDto.getEmailId().isEmpty()){
            throw new IllegalArgumentException("저장할 수 있는 이메일이 없습니다.");
        }
    }

    @Test
    void builder() {
    }
}