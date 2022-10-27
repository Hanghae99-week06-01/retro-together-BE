

package com.onetier.retro_together.repository;

import com.onetier.retro_together.domain.Member;
import com.onetier.retro_together.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByOrderByModifiedAtDesc();

    List<Post> findAllByMember(Member member);

}
