package app.rivalscope.marvel_rivals_reviews.player.dto;

import app.rivalscope.marvel_rivals_reviews.player.model.Player;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    Player toPlayer(PlayerCreateDto playerCreateDto);
}