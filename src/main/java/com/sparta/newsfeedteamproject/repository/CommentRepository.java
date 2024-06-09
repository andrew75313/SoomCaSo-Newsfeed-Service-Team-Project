package com.sparta.newsfeedteamproject.repository;

import com.sparta.newsfeedteamproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
