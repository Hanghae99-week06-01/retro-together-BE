package com.onetier.retro_together.repository;

import com.onetier.retro_together.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByImgURL(String imgUrl);
}
