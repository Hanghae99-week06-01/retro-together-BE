package com.onetier.retro_together.controller;

import com.onetier.retro_together.controller.response.ImageResponseDto;
import com.onetier.retro_together.controller.response.ResponseDto;
import com.onetier.retro_together.service.S3UploaderService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
public class S3Controller {
    private final S3UploaderService s3Uploader;

    @PostMapping("/api/auth/image")
    public ResponseDto<?> imageUpload(@RequestPart(value="file") MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) {
            return ResponseDto.fail("INVALIED_FILE", "파일이 유효하지 않습니다.");
        }
        try {
            return ResponseDto.success(new ImageResponseDto(s3Uploader.uploadFile(multipartFile, "image")));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.fail("INVALID_FILE" , "파일이 유효하지 않습니다.");
        }
    }
}
