package com.onetier.retro_together.ObjectException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * URLException 2022-10-23 오후 10시 30분 추가
 */
public class URLException {

    public static boolean URLException(String url) {
        try {
            new URL(url).toURI();
            return true;
        }catch(URISyntaxException | MalformedURLException exception) {
            throw new IllegalArgumentException("imgUrl 이 유효하지 않습니다.");
        }
    }

}
