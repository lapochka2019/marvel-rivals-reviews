package app.rivalscope.marvel_rivals_reviews.player.dto;

import app.rivalscope.marvel_rivals_reviews.player.model.Player;
import app.rivalscope.marvel_rivals_reviews.review.dto.ArrangeData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    Player toPlayer(PlayerCreateDto playerCreateDto);

    @Mapping(target = "avgRank", source = "arrangeData.avgRank")
    @Mapping(target = "avgGrade", source = "arrangeData.avgGrade")
    PlayerDto toPlayerDto(Player player, ArrangeData arrangeData);
}