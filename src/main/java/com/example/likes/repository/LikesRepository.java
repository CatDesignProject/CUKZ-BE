package com.example.likes.repository;

import com.example.likes.entity.Likes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByProductIdAndMemberId(Long productId, Long memberId);

    Page<Likes> findByMemberId(Long member_id, Pageable pageable);
}
