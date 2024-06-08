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

    @GetMapping("/{feedId}/comments/{commentId}")
    public ResponseEntity<BaseResDto<CommentResDto>> getComment(@PathVariable Long feedId,
                                                                @PathVariable Long commentId) {

        BaseResDto<CommentResDto> resDto = commentService.getComment(feedId, commentId);

        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }

    @PutMapping("/{feedId}/comments/{commentId}")
    public ResponseEntity<BaseResDto<CommentResDto>> updateComment(@PathVariable Long feedId,
                                                                   @PathVariable Long commentId,
                                                                   @Valid @RequestBody CommentReqDto reqDto,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {

        BaseResDto<CommentResDto> resDto = commentService.updateComment(feedId, commentId, reqDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(resDto);
    }
}
