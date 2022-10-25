package com.onetier.retro_together.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tagName;

    //Post와 Tag의 관계는 다대다 관계이다.
    //Post와 Tag의 관계를 관리하는 Post_Tag를 생성하여 다대다 관계를 일대다, 다대일 관계로 풀어낸다.
    @OneToMany(mappedBy = "tag")
    private List<Post_Tag> post_tagList;

    // get tag name
    public String getTagName() {
        return tagName;
    }
}