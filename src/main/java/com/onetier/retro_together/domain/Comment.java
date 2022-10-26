package com.onetier.retro_together.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onetier.retro_together.controller.request.CommentRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name ="member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(nullable = false)
    private String content;

    // 좋아요 카운트 추가 2022-10-25
    @Column
    private Long likeCount;

    // PostCommentLike 추가 2022-10-25
    @OneToMany(mappedBy = "comment", orphanRemoval = true)
    public List<PostCommentLike> postcommentLike;

    @JoinColumn(name = "parent_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Comment parent;

    @Builder.Default //warning: @Builder will ignore the initializing expression entirely. If you want the initializing expression to serve as default, add @Builder.Default. If it is not supposed to be settable during building, make the field final. 오류 때문에 추가함 2022- 10 - 25 오후 3시 48분
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }
}
