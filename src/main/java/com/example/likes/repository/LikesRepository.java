package com.example.likes.repository;

import com.example.likes.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Object> findByProductIdAndMemberId(Long productId, Long memberId);
}
