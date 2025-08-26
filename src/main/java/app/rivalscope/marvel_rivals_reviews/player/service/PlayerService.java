package app.rivalscope.marvel_rivals_reviews.player.service;

import app.rivalscope.marvel_rivals_reviews.player.dto.PlayerCreateDto;
import app.rivalscope.marvel_rivals_reviews.player.dto.PlayerDto;
import app.rivalscope.marvel_rivals_reviews.player.model.Player;
import org.apache.coyote.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlayerService {

    PlayerDto create(PlayerCreateDto player);

    PlayerDto updateImage(String name, MultipartFile image) throws BadRequestException;

    PlayerDto getPlayerByNick(String nick);

    List<Player> searchPlayerByNick(String substring);

    List<Player> getPlayers(int limit);
}

