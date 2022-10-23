package com.onetier.retro_together.repository;

import com.onetier.retro_together.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * PostRepository 수정해야함 7시 33분
 */

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByOrderByModifiedAtDesc();


}
