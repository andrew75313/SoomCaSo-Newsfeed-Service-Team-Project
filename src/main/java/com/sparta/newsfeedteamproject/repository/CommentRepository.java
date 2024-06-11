package com.sparta.newsfeedteamproject.repository;

import com.sparta.newsfeedteamproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByFeedId(Long feedId);
}