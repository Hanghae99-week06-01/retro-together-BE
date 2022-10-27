package com.onetier.retro_together.repository;

import com.onetier.retro_together.domain.Member;
import com.onetier.retro_together.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByMember(Member member);
  Optional<RefreshToken> findByValue(String value);
}
