package com.onetier.retro_together.repository;
import com.onetier.retro_together.domain.Member;
import com.onetier.retro_together.domain.PostCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * PostCommentLikeRepository 2022-10-25 추가
 */
public interface PostCommentLikeRepository extends JpaRepository<PostCommentLike,Long>  {

    Long countAllByCommentId(Long commentId);

    PostCommentLike findByCommentIdAndMemberId(Long commentId, Long memberId);

    List<PostCommentLike> findAllByMember(Member member); // 2022 - 10 - 27 mypage 추가
}
