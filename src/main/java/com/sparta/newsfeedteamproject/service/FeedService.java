package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.BaseResDto;
import com.sparta.newsfeedteamproject.dto.comment.CommentResDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedReqDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedResDto;
import com.sparta.newsfeedteamproject.entity.Comment;
import com.sparta.newsfeedteamproject.entity.Contents;
import com.sparta.newsfeedteamproject.entity.Feed;
import com.sparta.newsfeedteamproject.entity.User;
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

    public BaseResDto<List<FeedResDto>> getAllFeeds(int page, String sortBy, LocalDate startDate, LocalDate endDate) {

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
            return new BaseResDto<>(HttpStatus.OK.value(), "먼저 작성하여 소식을 알려보세요!", null);
        }

        feedList.forEach(feedResDto -> feedResDto.setComments(null));

        return new BaseResDto<>(HttpStatus.OK.value(), "게시물 조회가 완료되었습니다!", feedList);
    }

    public BaseResDto<FeedResDto> getFeed(Long feed_id) {

        FeedResDto feedResDto = new FeedResDto(findFeed(feed_id));
        List<CommentResDto> commentResDtoList = commentRepository.findAllByFeedId(feed_id).stream()
                .map(CommentResDto::new)
                .toList();

        if (commentResDtoList.isEmpty()) {
            feedResDto.setComments(null);
        } else {
            feedResDto.setComments(commentResDtoList);
        }

        return new BaseResDto<>(HttpStatus.OK.value(), "게시물 조회가 완료되었습니다!", feedResDto);
    }

    public BaseResDto<FeedResDto> createFeed(FeedReqDto reqDto, User user) {

        Feed feed = feedRepository.save(new Feed(reqDto, user));

        return new BaseResDto<>(HttpStatus.OK.value(), "게시물 작성이 완료되었습니다!", new FeedResDto(feed));
    }

    @Transactional
    public BaseResDto<FeedResDto> updateFeed(Long feed_id, FeedReqDto reqDto, User user) {

        Feed feed = findFeed(feed_id);

        if (!feed.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 게시물은 작성자만 수정/삭제 할 수 있습니다!");
        }

        feed.update(reqDto);

        return new BaseResDto<>(HttpStatus.OK.value(), "게시물 수정이 완료되었습니다!", new FeedResDto(feed));
    }

    public BaseResDto<FeedResDto> deleteFeed(Long feed_id, User user) {

        Feed feed = findFeed(feed_id);

        if (!feed.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 게시물은 작성자만 수정/삭제 할 수 있습니다!");
        }

        feedRepository.delete(feed);

        likeRepository.findAllByContentsIdAndContents(feed_id, Contents.FEED)
                .ifPresent(likes -> likes.stream()
                        .forEach(like -> likeRepository.delete(like)));

        List<Comment> commentList = commentRepository.findAllByFeedId(feed_id);

        commentList.forEach(comment -> {
            likeRepository.findAllByContentsIdAndContents(comment.getId(), Contents.COMMENT)
                    .ifPresent(likes -> likes.forEach(like -> likeRepository.delete(like)));
        });

        return new BaseResDto<>(HttpStatus.OK.value(), "게시물 삭제가 완료되었습니다!", null);
    }

    @Transactional
    public void increaseFeedLikes(Long feed_id) {

        Feed feed = findFeed(feed_id);

        feed.increaseLikes();
    }

    @Transactional
    public void decreaseFeedLikes(Long feed_id) {

        Feed feed = findFeed(feed_id);

        feed.decreaseLikes();
    }

    public Feed findFeed(Long feed_id) {

        Feed feed = feedRepository.findById(feed_id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다!")
        );

        return feed;
    }
}