package com.onetier.retro_together.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PostCategory {
    WIL("WIL"),
    TIL("TIL");

    private String value;


    PostCategory(String value) {
        this.value = value;
    };

    public static PostCategory fromCode(String dbData){
        return Arrays.stream(PostCategory.values())
                .filter(v -> v.getValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("해당카테고리가 존재하지않습니다",dbData)));
    };


}