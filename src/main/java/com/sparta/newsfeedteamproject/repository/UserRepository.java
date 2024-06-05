package com.sparta.newsfeedteamproject.repository;

import com.sparta.newsfeedteamproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByLoginId(String login_id);

    Optional<User> findByEmail(String email);
}

