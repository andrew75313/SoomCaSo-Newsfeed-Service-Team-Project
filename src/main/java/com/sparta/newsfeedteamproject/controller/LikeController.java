package com.sparta.newsfeedteamproject.controller;

import com.sparta.newsfeedteamproject.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class LikeController {

    private final LikeService likeService;
}
