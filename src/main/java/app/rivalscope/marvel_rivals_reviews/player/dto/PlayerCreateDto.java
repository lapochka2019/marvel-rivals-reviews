package app.rivalscope.marvel_rivals_reviews.player.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerCreateDto {
    @NotBlank(message = "Ник игрока не может быть пустым")
    private String nickName;
}
