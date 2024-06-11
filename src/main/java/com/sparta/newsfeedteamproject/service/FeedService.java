package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.MessageResDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentResDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedReqDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedResDto;
import com.sparta.newsfeedteamproject.entity.Comment;
import com.sparta.newsfeedteamproject.entity.Contents;
import com.sparta.newsfeedteamproject.entity.Feed;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.exception.ExceptionMessage;
import com.sparta.newsfeedteamproject.repository.CommentRepository;
import com.sparta.newsfeedteamproject.repository.FeedRepository;
import com.sparta.newsfeedteamproject.repository.LikeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeedService {

    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public FeedService(FeedRepository feedRepository, CommentRepository commentRepository, LikeRepository likeRepository) {
        this.feedRepository = feedRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
    }

    public MessageResDto<List<FeedResDto>> getAllFeeds(int page, String sortBy, LocalDate startDate, LocalDate endDate) {

        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<FeedResDto> feedPage;

        if (startDate != null && endDate != null) {
            feedPage = feedRepository.findAllByCreatedAtBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay(), pageable)
                    .map(FeedResDto::new);
        } else {
            feedPage = feedRepository.findAll(pageable).map(FeedResDto::new);
        }

        List<FeedResDto> feedList = feedPage.getContent();

        if (feedList.isEmpty()) {
            return new MessageResDto<>(HttpStatus.OK.value(), "먼저 작성하여 소식을 알려보세요!", null);
        }

        feedList.forEach(feedResDto -> feedResDto.setComments(null));

        return new MessageResDto<>(HttpStatus.OK.value(), "게시물 조회가 완료되었습니다!", feedList);
    }

    public MessageResDto<FeedResDto> getFeed(Long feedId) {

        FeedResDto feedResDto = new FeedResDto(findFeed(feedId));
        List<CommentResDto> commentResDtoList = commentRepository.findAllByFeedId(feedId).stream()
                .map(CommentResDto::new)
                .toList();

        if (commentResDtoList.isEmpty()) {
            feedResDto.setComments(null);
        } else {
            feedResDto.setComments(commentResDtoList);
        }

        return new MessageResDto<>(HttpStatus.OK.value(), "게시물 조회가 완료되었습니다!", feedResDto);
    }

    public MessageResDto<FeedResDto> createFeed(FeedReqDto reqDto, User user) {

        Feed feed = feedRepository.save(new Feed(reqDto, user));

        return new MessageResDto<>(HttpStatus.OK.value(), "게시물 작성이 완료되었습니다!", new FeedResDto(feed));
    }

    @Transactional
    public MessageResDto<FeedResDto> updateFeed(Long feedId, FeedReqDto reqDto, User user) {

        Feed feed = findFeed(feedId);

        if (!feed.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException(ExceptionMessage.DIFFERENT_WRITER.getExceptionMessage());
        }

        feed.update(reqDto);

        return new MessageResDto<>(HttpStatus.OK.value(), "게시물 수정이 완료되었습니다!", new FeedResDto(feed));
    }

    public MessageResDto<FeedResDto> deleteFeed(Long feedId, User user) {

        Feed feed = findFeed(feedId);

        if (!feed.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException(ExceptionMessage.DIFFERENT_WRITER.getExceptionMessage());
        }

        likeRepository.findAllByContentsIdAndContents(feedId, Contents.FEED)
                .ifPresent(likes -> likes.stream()
                        .forEach(like -> likeRepository.delete(like)));

        List<Comment> commentList = commentRepository.findAllByFeedId(feedId);

        commentList.forEach(comment -> {
            likeRepository.findAllByContentsIdAndContents(comment.getId(), Contents.COMMENT)
                    .ifPresent(likes -> likes.forEach(like -> likeRepository.delete(like)));
        });

        feedRepository.delete(feed);

        return new MessageResDto<>(HttpStatus.OK.value(), "게시물 삭제가 완료되었습니다!", null);
    }

    @Transactional
    public void increaseFeedLikes(Long feedId) {

        Feed feed = findFeed(feedId);

        feed.increaseLikes();
    }

    @Transactional
    public void decreaseFeedLikes(Long feedId) {

        Feed feed = findFeed(feedId);

        feed.decreaseLikes();
    }

    public Feed findFeed(Long feedId) {

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new IllegalArgumentException(ExceptionMessage.NON_EXISTENT_ELEMENT.getExceptionMessage())
        );

        return feed;
    }
}