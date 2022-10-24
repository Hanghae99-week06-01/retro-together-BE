package com.onetier.retro_together.domain;

import com.onetier.retro_together.controller.request.PostRequestDto;
import java.util.List;
import javax.persistence.*;

import com.onetier.retro_together.controller.response.ImageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Post
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

    //2022- 10 -24 추가
    @OneToMany(mappedBy = "post", fetch= FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    /**
     * Update
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

    @Column(nullable = false) // comment_cnt_up 추가 오후 3시 14분
    private Integer comment_cnt = 0;

    /**
     * comment_cnt_up 추가 2022-10-24 오후 3시 12분
     */
    public void comment_cnt_Up() {
        this.comment_cnt++;
    }
    /**
     * comment_cnt_Down 추가 2022-10-24 오후 3시 15분
     */
    public void comment_cnt_Down() {
        this.comment_cnt--;
    }
}


