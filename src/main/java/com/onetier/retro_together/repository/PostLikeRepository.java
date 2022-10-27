package com.onetier.retro_together.repository;

import com.onetier.retro_together.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PostLikeRepository 추가 2022-10-25
 */

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Long countAllByPostId(Long postId);

    PostLike findByPostIdAndMemberId(Long postId, Long memberId);

}
