package app.rivalscope.marvel_rivals_reviews.player.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {
    Long id;
    String nickName;
    Integer owner;
    LocalDateTime created;
    String image;
    Double avgRank;
    Double avgGrade;
}
