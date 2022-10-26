package com.onetier.retro_together.repository;
import com.onetier.retro_together.domain.PostCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PostCommentLikeRepository 2022-10-25 추가
 */
public interface PostCommentLikeRepository extends JpaRepository<PostCommentLike,Long>  {

    Long countAllByCommentId(Long commentId);

    PostCommentLike findByCommentIdAndMemberId(Long commentId, Long memberId);

}
