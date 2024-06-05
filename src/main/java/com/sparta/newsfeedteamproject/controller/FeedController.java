package com.sparta.newsfeedteamproject.controller;

import com.sparta.newsfeedteamproject.dto.BaseResDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedResDto;
import com.sparta.newsfeedteamproject.service.FeedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/feed/all")
    public ResponseEntity<BaseResDto<List<FeedResDto>>> getAllFeeds() {
        BaseResDto<List<FeedResDto>> response =  feedService.getAllFeeds();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
