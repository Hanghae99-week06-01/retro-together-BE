package com.onetier.retro_together.domain;

import com.onetier.retro_together.controller.request.PostRequestDto;
import com.onetier.retro_together.controller.response.ImageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    // 좋아요 카운트 추가 2022-10-25
    @Column
    private Long likeCount;

    // 게시글 좋아요 추가 2022-10-25
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLike;

    @Column
    private String image;

    //2022- 10 -24 추가
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

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
     *
     * @param member
     * @return
     * @author doosan
     */
    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

    // comment_cnt_up 추가 오후 3시 14분
    // comment_cnt 타입을 Integer -> int로 변경 및 nullable false 삭제

    @Builder.Default // warning: @Builder will ignore the initializing expression entirely. If you want the initializing expression to serve as default, add @Builder.Default. If it is not supposed to be settable during building, make the field final. 오류 때문에 추가함 2022-10-25 오후 3시 47분
    @Column(nullable = true)
    private int comment_cnt = 0;

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