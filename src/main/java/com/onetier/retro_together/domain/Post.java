package com.onetier.retro_together.domain;

import com.onetier.retro_together.controller.request.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor // 기본 생성자를 만듭니다.
@Getter
@Entity // 테이블과 연계됨을 스프링에게 알려줍니다.
@EntityListeners(AuditingEntityListener.class) // 생성/변경 시간을 자동으로 업데이트합니다.
/* @MappedSuperclass // Entity가 자동으로 컬럼으로 인식합니다. 물어보기(이 어노테이션은 객체의
  입장에서 공통 매핑 정보가 필요할 때 사용한다. 생성시간과 수정시간에 보통쓰이는것 같은데 어떻게 해야할까*/
public class Post {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private int postCount;

    @Id
    private Long author;

    @Id
    private Long comment;

    @Id
    private Long tag;

    @Id
    private Long file;

    public Post(PostRequestDto requestDto) {
        this.postId = requestDto.getPostId();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.postCount = requestDto.getPostCount();
    }

    public void update(PostRequestDto requestDto) {
        this.postId = requestDto.getPostId();
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.postCount = requestDto.getPostCount();
    }
}
