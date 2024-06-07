package com.sparta.newsfeedteamproject.controller;

import com.sparta.newsfeedteamproject.dto.BaseResDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedReqDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedResDto;
import com.sparta.newsfeedteamproject.security.UserDetailsImpl;
import com.sparta.newsfeedteamproject.service.FeedService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/all")
    public ResponseEntity<BaseResDto<List<FeedResDto>>> getAllFeeds() {

        BaseResDto<List<FeedResDto>> response = feedService.getAllFeeds();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{feed_id}")
    public ResponseEntity<BaseResDto<FeedResDto>> getFeed(@PathVariable(name = "feed_id") Long feed_id) {

        BaseResDto<FeedResDto> response = feedService.getFeed(feed_id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<BaseResDto<FeedResDto>> createFeed(@Valid @RequestBody FeedReqDto reqDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        BaseResDto<FeedResDto> response = feedService.createFeed(reqDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{feed_id}")
    public ResponseEntity<BaseResDto<FeedResDto>> updateFeed(@PathVariable(name = "feed_id") Long feed_id,
                                                             @Valid @RequestBody FeedReqDto reqDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        BaseResDto<FeedResDto> response = feedService.updateFeed(feed_id, reqDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{feed_id}")
    public ResponseEntity<BaseResDto<FeedResDto>> deleteFeed(@PathVariable(name = "feed_id") Long feed_id,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        BaseResDto<FeedResDto> response = feedService.deleteFeed(feed_id, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
