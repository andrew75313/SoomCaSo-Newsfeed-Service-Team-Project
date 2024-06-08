package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.BaseResDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentReqDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentResDto;
import com.sparta.newsfeedteamproject.entity.Comment;
import com.sparta.newsfeedteamproject.entity.Feed;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.repository.CommentRepository;
import com.sparta.newsfeedteamproject.repository.FeedRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;

    public CommentService(CommentRepository commentRepository, FeedRepository feedRepository) {
        this.commentRepository = commentRepository;
        this.feedRepository = feedRepository;
    }

    public BaseResDto<CommentResDto> createComment(Long feedId, CommentReqDto reqDto, User user) {

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 Feed 입니다.")
        );

        Comment comment = new Comment(reqDto, feed, user, 0L);
        Comment saveComment = commentRepository.save(comment);
        CommentResDto resDto = new CommentResDto(saveComment);

        return new BaseResDto<>(HttpStatus.OK.value(), "댓글 작성이 완료되었습니다!", resDto);
    }
}
