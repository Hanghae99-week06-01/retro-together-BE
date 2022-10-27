package com.onetier.retro_together.repository;

import com.onetier.retro_together.domain.Comment;
import com.onetier.retro_together.domain.Member;
import com.onetier.retro_together.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    List<Comment> findAllByPostAndParent(Post post, Comment parent);

    List<Comment> findAllByMember(Member member); // 2022-10-27 mypage추가

}
