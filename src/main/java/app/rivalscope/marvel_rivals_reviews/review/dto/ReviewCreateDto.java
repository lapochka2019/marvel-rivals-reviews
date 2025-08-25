package app.rivalscope.marvel_rivals_reviews.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateDto {

    String review;
    Integer grade;
    Integer rank;
    Long playerId;

}
