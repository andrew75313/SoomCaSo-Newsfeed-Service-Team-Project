package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.BaseResDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentReqDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentResDto;
import com.sparta.newsfeedteamproject.entity.Comment;
import com.sparta.newsfeedteamproject.entity.Feed;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedService feedService;

    public CommentService(CommentRepository commentRepository, FeedService feedService) {
        this.commentRepository = commentRepository;
        this.feedService = feedService;
    }

    public BaseResDto<CommentResDto> createComment(Long feedId, CommentReqDto reqDto, User user) {

        Feed feed = feedService.findFeed(feedId);
        Comment comment = new Comment(reqDto, feed, user, 0L);
        Comment saveComment = commentRepository.save(comment);
        CommentResDto resDto = new CommentResDto(saveComment);

        return new BaseResDto<>(HttpStatus.OK.value(), "댓글 작성이 완료되었습니다!", resDto);
    }

    public BaseResDto<CommentResDto> getComment(Long feedId, Long commentId) {

        feedService.findFeed(feedId);
        Comment comment = findComment(commentId);
        CommentResDto resDto = new CommentResDto(comment);

        return new BaseResDto<>(HttpStatus.OK.value(), "댓글 조회가 완료되었습니다!", resDto);
    }

    private Comment findComment(Long id) {

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다!")
        );

        return comment;
    }
}
