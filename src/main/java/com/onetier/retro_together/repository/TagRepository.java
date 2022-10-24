package com.onetier.retro_together.repository;

import com.onetier.retro_together.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TagRepository
 */

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByTagName(String tagName);
}
