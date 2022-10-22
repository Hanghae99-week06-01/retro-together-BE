package com.onetier.retro_together.repository;

import com.onetier.retro_together.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findById(Long id);
    Optional<Member> findByEmailId(String emailId);
    Optional<Member> findByNickname(String nickname);


}
