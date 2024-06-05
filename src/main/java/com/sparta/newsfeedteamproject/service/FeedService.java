package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.BaseResDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedReqDto;
import com.sparta.newsfeedteamproject.dto.feed.FeedResDto;
import com.sparta.newsfeedteamproject.entity.Feed;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.repository.FeedRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedService {

    private final FeedRepository feedRepository;

    public FeedService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public BaseResDto<List<FeedResDto>> getAllFeeds() {

        List<FeedResDto> feedList = feedRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(feed -> new FeedResDto(feed))
                .collect(Collectors.toList());

        String message = feedList.isEmpty() ? "게시물 조회가 완료되었습니다!" : "먼저 작성하여 소식을 알려보세요!";

        return new BaseResDto<>(HttpStatus.OK.value(), message, feedList);
    }

    public BaseResDto<FeedResDto> getFeed(Long feedId) {

        Feed feed = feedRepository.findById(feed_id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다!")
        );

        return new BaseResDto<>(HttpStatus.OK.value(), "게시물 조회가 완료되었습니다!", new FeedResDto(feed));
    }

    public BaseResDto<FeedResDto> createFeed(FeedReqDto reqDto, User user) {

        Feed feed = feedRepository.save(new Feed(reqDto, user));

        return new BaseResDto<>(HttpStatus.OK.value(), "게시물 작성이 완료되었습니다!", new FeedResDto(feed));
    }

    @Transactional
    public BaseResDto<FeedResDto> updateFeed(Long feed_id, FeedReqDto reqDto, User user) {

        Feed feed = feedRepository.findById(feed_id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다!")
        );

        if (feed.getUser().getId() == user.getId()) {
            throw new IllegalArgumentException("해당 게시물은 작성자만 수정/삭제 할 수 있습니다!");
        }

        feed.update(reqDto);

        return new BaseResDto<>(HttpStatus.OK.value(), "게시물 수정이 완료되었습니다!", new FeedResDto(feed));
    }

    public BaseResDto<FeedResDto> deleteFeed(Long feed_id, User user) {

        Feed feed = feedRepository.findById(feed_id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다!")
        );

        if (feed.getUser().getId() == user.getId()) {
            throw new IllegalArgumentException("해당 게시물은 작성자만 수정/삭제 할 수 있습니다!");
        }

        feedRepository.delete(feed);

        return new BaseResDto<>(HttpStatus.OK.value(), "게시물 삭제가 완료되었습니다!", null);
    }
}