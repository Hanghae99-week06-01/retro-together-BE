package com.onetier.retro_together.domain;


import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostTag {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "post_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @JoinColumn(name = "tag_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    public Tag getTag() {
        return tag;
    }


}
