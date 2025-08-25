package app.rivalscope.marvel_rivals_reviews.review.dto;

import app.rivalscope.marvel_rivals_reviews.user.User;
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
public class ReviewResponseWithOwnerDto {

    Long id;
    String review;
    LocalDateTime created;
    Integer grade;
    Integer rank;
    String image;
    User owner;
}
