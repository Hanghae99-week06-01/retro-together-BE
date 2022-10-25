package com.onetier.retro_together.repository;

import com.onetier.retro_together.domain.Post;
import com.onetier.retro_together.domain.Post_Tag;
import com.onetier.retro_together.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * PostRepository 수정해야함 7시 33분
 */

public interface Post_TagRepository extends JpaRepository<Post_Tag,Long> {
    List<Post_Tag> findAllByPost(Post post);
    List<Post_Tag> findAllByTag(Tag tag);


}
