package app.rivalscope.marvel_rivals_reviews.review.service;

import app.rivalscope.marvel_rivals_reviews.review.dto.*;
import app.rivalscope.marvel_rivals_reviews.review.model.Review;
import org.apache.coyote.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    Review create(ReviewCreateDto createDto, Long userId);

    Review updateImage(Long reviewId, MultipartFile file, Long userId) throws BadRequestException;

    List<ReviewResponseWithOwnerDto> getReviewByPlayer (String nick);

    List<ReviewResponseWithPlayerDto> getReviewByUser (Long userId);

    ArrangeData getMiddleRankByPlayer(Long playerId);

}
