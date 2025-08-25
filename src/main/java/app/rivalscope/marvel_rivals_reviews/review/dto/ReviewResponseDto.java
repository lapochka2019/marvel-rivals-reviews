package app.rivalscope.marvel_rivals_reviews.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    Long id;
    String review;
    LocalDateTime created;
    Integer grade;
    Integer rank;
    String image;
}
