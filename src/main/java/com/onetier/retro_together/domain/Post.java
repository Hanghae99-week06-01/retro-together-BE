package com.onetier.retro_together.domain;
import com.onetier.retro_together.controller.request.PostRequestDto;
import com.onetier.retro_together.controller.response.ImageResponseDto;
import lombok.*;
import javax.persistence.*;
import java.util.List;

/**
 * Post
 */
@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Post extends Timestamped {
    // 대댓글 구현 중 다시 post 수정 2022-10-23 오후 6시 47분
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String image;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    //Tag와 Post의 관계
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<PostTag> postTagList;


    /**
     * Update
     *
     * @param postRequestDto
     * @param imageResponseDto
     * @author doosan
     */
    public void update(PostRequestDto postRequestDto, ImageResponseDto imageResponseDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.image = imageResponseDto.getImageUrl();
    }

    /**
     * memeber 유효성 체크
     * @param member
     * @return
     * @author doosan
     */
    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

    public void addPostTag(Tag tag) {
        PostTag postTag = PostTag.builder()
                .post(this)
                .tag(tag)
                .build();
        postTagList.add(postTag);
    }
}


