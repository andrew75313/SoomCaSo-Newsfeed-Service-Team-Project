package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.BaseResDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentDelResDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentReqDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentResDto;
import com.sparta.newsfeedteamproject.entity.Comment;
import com.sparta.newsfeedteamproject.entity.Contents;
import com.sparta.newsfeedteamproject.entity.Feed;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.repository.CommentRepository;
import com.sparta.newsfeedteamproject.repository.LikeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final FeedService feedService;

    public CommentService(CommentRepository commentRepository, LikeRepository likeRepository, FeedService feedService) {
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
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

    @Transactional
    public BaseResDto<CommentResDto> updateComment(Long feedId, Long commentId, CommentReqDto reqDto, User user) {

        feedService.findFeed(feedId);
        Comment comment = findComment(commentId);
        String loginUsername = user.getUsername();
        String commentUsername = comment.getUser().getUsername();

        if (!loginUsername.equals(commentUsername)) {
            throw new IllegalArgumentException("해당 댓글은 작성자만 수정 할 수 있습니다!");
        }

        comment.update(reqDto.getContents());
        CommentResDto resDto = new CommentResDto(comment);

        return new BaseResDto<>(HttpStatus.OK.value(), "댓글 수정이 완료되었습니다!", resDto);
    }

    public BaseResDto<CommentDelResDto> deleteComment(Long feedId, Long commentId, User user) {

        feedService.findFeed(feedId);
        Comment comment = findComment(commentId);
        String loginUsername = user.getUsername();
        String commentUsername = comment.getUser().getUsername();

        if (!loginUsername.equals(commentUsername)) {
            throw new IllegalArgumentException("해당 댓글은 작성자만 삭제 할 수 있습니다!");
        }

        deleteLikes(commentId);
        commentRepository.delete(comment);

        CommentDelResDto resDto = new CommentDelResDto(commentId);

        return new BaseResDto<>(HttpStatus.OK.value(), "댓글 삭제가 완료되었습니다!", resDto);
    }

    private Comment findComment(Long id) {

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다!")
        );

        return comment;
    }

    // 댓글 삭제 시 해당 댓글의 좋아요를 모두 삭제하는 메서드
    private void deleteLikes(Long contentsId) {

        likeRepository.findAllByContentsIdAndContents(contentsId, Contents.COMMENT)
                .ifPresent(likes -> likes.stream()
                        .forEach(like -> likeRepository.delete(like)));
    }
}
