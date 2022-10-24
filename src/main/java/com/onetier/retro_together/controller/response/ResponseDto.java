package com.onetier.retro_together.controller.response;
import com.onetier.retro_together.domain.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
  private boolean success;
  private T data;
  private Error error;

  public static <T> ResponseDto<T> success(T data) {
    return new ResponseDto<>(true, data, null);
  }

  public static <T> ResponseDto<T> fail(String code, String message) {
    return new ResponseDto<>(false, null, new Error(code, message));
  }
  // 2022-10-24  오후 5시 31분 추가
  @Getter
  @AllArgsConstructor
  static class Error {
    private String code;
    private String message;
  }
}
