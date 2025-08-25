package app.rivalscope.marvel_rivals_reviews.review.dto;

import app.rivalscope.marvel_rivals_reviews.player.model.Player;
import app.rivalscope.marvel_rivals_reviews.user.User;
import app.rivalscope.marvel_rivals_reviews.review.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", source = "user")
    @Mapping(target = "player", source = "player")
    Review toReview (ReviewCreateDto reviewCreateDto, User user, Player player, LocalDateTime created);

    ReviewResponseDto toReviewResponseDto (Review review);
    ReviewResponseWithPlayerDto toReviewResponseWithPlayerDto (Review review);
    ReviewResponseWithOwnerDto toReviewResponseWithOwnerDto (Review review);
}
