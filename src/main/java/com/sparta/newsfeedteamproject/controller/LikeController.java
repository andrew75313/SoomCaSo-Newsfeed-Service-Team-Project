package com.sparta.newsfeedteamproject.controller;

import com.sparta.newsfeedteamproject.dto.LikeResDto;
import com.sparta.newsfeedteamproject.dto.MessageResDto;
import com.sparta.newsfeedteamproject.security.UserDetailsImpl;
import com.sparta.newsfeedteamproject.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{feedId}/like")
    public ResponseEntity<MessageResDto<LikeResDto>> likeFeed(@PathVariable("feedId") @Valid Long feedId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        MessageResDto<LikeResDto> response = likeService.likeFeed(feedId, userDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{feedId}/comments/{commentId}/like")
    public ResponseEntity<MessageResDto<LikeResDto>> likeComment(@PathVariable Long feedId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        MessageResDto<LikeResDto> reponse = likeService.likeComment(feedId, commentId, userDetails);
        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }
}
