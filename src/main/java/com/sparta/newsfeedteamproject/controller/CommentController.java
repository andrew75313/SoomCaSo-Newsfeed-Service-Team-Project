package com.sparta.newsfeedteamproject.controller;

import com.sparta.newsfeedteamproject.dto.BaseResDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentReqDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentResDto;
import com.sparta.newsfeedteamproject.security.UserDetailsImpl;
import com.sparta.newsfeedteamproject.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feeds")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{feedId}/comments")
    public ResponseEntity<BaseResDto<CommentResDto>> createComment(@PathVariable Long feedId,
                                                                   @Valid @RequestBody CommentReqDto reqDto,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {

        BaseResDto<CommentResDto> resDto = commentService.createComment(feedId, reqDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }
}
