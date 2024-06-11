package com.sparta.newsfeedteamproject.repository;

import com.sparta.newsfeedteamproject.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    Page<Feed> findAllByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}