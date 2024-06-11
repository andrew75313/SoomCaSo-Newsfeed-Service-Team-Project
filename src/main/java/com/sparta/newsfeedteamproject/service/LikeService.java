package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.LikeResDto;
import com.sparta.newsfeedteamproject.dto.MessageResDto;
import com.sparta.newsfeedteamproject.entity.Contents;
import com.sparta.newsfeedteamproject.entity.Like;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.exception.ExceptionMessage;
import com.sparta.newsfeedteamproject.repository.LikeRepository;
import com.sparta.newsfeedteamproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final FeedService feedService;
    private final CommentService commentService;

    public MessageResDto<LikeResDto> likeFeed(Long feedId, UserDetailsImpl userDetails) {
        feedService.findFeed(feedId);
        User user = userDetails.getUser();
        Optional<Like> like = likeRepository.findByContentsIdAndContentsAndUser(feedId, Contents.FEED, user);
        LikeResDto likeResDto = new LikeResDto(feedId, Contents.FEED);

        if (like.isEmpty()) { //좋아요 등록
            Like newlike = new Like(user, feedId, Contents.FEED);
            likeRepository.save(newlike);

            //게시물 DB에 저장된 좋아요 수 +1
            feedService.increaseFeedLikes(feedId); //FeedSerivce에 구현되어야함

            return new MessageResDto(HttpStatus.OK.value(), "게시글을 좋아요하였습니다!", likeResDto);

        } else {//좋아요 취소
            Like oldLike = like.orElseThrow(
                    () -> new IllegalArgumentException(ExceptionMessage.NON_EXISTENT_ELEMENT.getExceptionMessage())
            );
            likeRepository.delete(oldLike);

            //게시물 DB에 저장된 좋아요 수 -1
            feedService.decreaseFeedLikes(feedId); //FeedSerivce에 구현되어야함
            return new MessageResDto(HttpStatus.OK.value(), "게시글 좋아요를 취소하였습니다!", likeResDto);
        }
    }

    public MessageResDto<LikeResDto> likeComment(Long feedId, Long commentId, UserDetailsImpl userDetails) {
        feedService.findFeed(feedId);
        commentService.findComment(commentId);

        User user = userDetails.getUser();
        Optional<Like> like = likeRepository.findByContentsIdAndContentsAndUser(commentId, Contents.COMMENT, user);
        LikeResDto likeResDto = new LikeResDto(commentId, Contents.COMMENT);

        if (like.isEmpty()) {
            Like newlike = new Like(user, commentId, Contents.COMMENT);
            likeRepository.save(newlike);

            commentService.increaseCommentLikes(commentId);

            return new MessageResDto<>(HttpStatus.OK.value(), "댓글을 좋아요하였습니다!", likeResDto);

        } else {
            Like oldLike = like.orElseThrow(
                    () -> new IllegalArgumentException(ExceptionMessage.NON_EXISTENT_ELEMENT.getExceptionMessage())
            );
            likeRepository.delete(oldLike);

            commentService.decreaseCommentLikes(commentId);
            return new MessageResDto<>(HttpStatus.OK.value(), "댓글 좋아요를 취소하였습니다.", likeResDto);
        }
    }

    //게시글 or 댓글 삭제 시 해당 게시글 or 댓글의 좋아요를 모두 삭제하는 메서드
    public void deleteAllLikes(Long contentsId, Contents contentType) {
        likeRepository.findAllByContentsIdAndContents(contentsId, contentType)
                .ifPresent(likes -> likes.stream()
                        .forEach(like -> likeRepository.delete(like)));
    }

    //게시글 삭제 시 해당 게시글의 댓글들의 좋아요를 모두 삭제하는 메서드
    public void deleteAllCommentsLikes(List<Long> contentsIds, Contents contentType) {
        contentsIds.stream()
                .forEach(id -> deleteAllLikes(id, contentType));
    }
}
