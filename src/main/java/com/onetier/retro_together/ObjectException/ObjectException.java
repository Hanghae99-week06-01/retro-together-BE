package com.onetier.retro_together.ObjectException;

import com.onetier.retro_together.domain.Post;

/**
 * ObjectException 2022-10-23 오후 10시 28분 추가
 */
public class ObjectException {

    public static void postValidate(Post post) {
        if(post.getId()==null || post.getId()<=0) {
            throw new IllegalArgumentException("유효하지 않는 Post ID 입니다.");
        }
    }

}
