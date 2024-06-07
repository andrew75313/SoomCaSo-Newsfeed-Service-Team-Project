package com.sparta.newsfeedteamproject.repository;

import com.sparta.newsfeedteamproject.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
}
