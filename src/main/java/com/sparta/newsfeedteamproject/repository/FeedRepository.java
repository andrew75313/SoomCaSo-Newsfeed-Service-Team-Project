package com.sparta.newsfeedteamproject.repository;

import com.sparta.newsfeedteamproject.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
