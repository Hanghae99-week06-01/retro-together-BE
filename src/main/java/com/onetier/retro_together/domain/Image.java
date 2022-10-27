package com.onetier.retro_together.domain;

import com.onetier.retro_together.ObjectException.ObjectException;
import com.onetier.retro_together.ObjectException.URLException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Image class 추가 2022- 10 -23 오후 8시 1분
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne // OneToMany -> OneToOne으로 수정 2022-10-23 오후 10시 38분
    @JoinColumn(name="post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String imgURL;

    public Image(Post post, String imageUrl) {
        URLException.URLException(imageUrl);
        ObjectException.postValidate(post);
        this.post= post;
        this.imgURL = imageUrl;
    }
}
