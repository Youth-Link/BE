package com.youthlink.server.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youthlink.server.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
