package app.rivalscope.marvel_rivals_reviews.review.dto;

import app.rivalscope.marvel_rivals_reviews.player.model.Player;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponseWithPlayerDto {

    Long id;
    String review;
    LocalDateTime created;
    Integer grade;
    Integer rank;
    String image;
    Player player;
}
