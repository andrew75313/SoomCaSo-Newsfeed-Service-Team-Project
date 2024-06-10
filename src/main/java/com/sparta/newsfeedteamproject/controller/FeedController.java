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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/all")
    public ResponseEntity<BaseResDto<List<FeedResDto>>> getAllFeeds(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                    @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                                                    @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                                                    @RequestParam(value = "endDate", required = false) LocalDate endDate) {

        BaseResDto<List<FeedResDto>> response = feedService.getAllFeeds(page - 1, sortBy, startDate, endDate);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/{feedId}")
    public ResponseEntity<BaseResDto<FeedResDto>> getFeed(@PathVariable(name = "feedId") Long feedId) {

        BaseResDto<FeedResDto> response = feedService.getFeed(feedId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<BaseResDto<FeedResDto>> createFeed(@Valid @RequestBody FeedReqDto reqDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        BaseResDto<FeedResDto> response = feedService.createFeed(reqDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{feedId}")
    public ResponseEntity<BaseResDto<FeedResDto>> updateFeed(@PathVariable(name = "feedId") Long feedId,
                                                             @Valid @RequestBody FeedReqDto reqDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        BaseResDto<FeedResDto> response = feedService.updateFeed(feedId, reqDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<BaseResDto<FeedResDto>> deleteFeed(@PathVariable(name = "feedId") Long feedId,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        BaseResDto<FeedResDto> response = feedService.deleteFeed(feedId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
