package com.sparta.newsfeedteamproject.repository;

import com.sparta.newsfeedteamproject.entity.Contents;
import com.sparta.newsfeedteamproject.entity.Like;
import com.sparta.newsfeedteamproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByContentsIdAndContentsAndUser(Long contentId, Contents contents, User user);
    Optional<List<Like>> findAllByContentsIdAndContents(Long contentId, Contents contents);
}