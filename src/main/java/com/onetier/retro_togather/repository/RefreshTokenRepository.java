package com.onetier.retro_togather.repository;

import com.onetier.retro_togather.domain.Member;
import com.onetier.retro_togather.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByMember(Member member);
}
