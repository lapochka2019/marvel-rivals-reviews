package app.rivalscope.marvel_rivals_reviews.player.service;

import app.rivalscope.marvel_rivals_reviews.player.dto.PlayerCreateDto;
import app.rivalscope.marvel_rivals_reviews.player.model.Player;
import org.apache.coyote.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlayerService {

    Player create(PlayerCreateDto player);

    Player updateImage(String name, MultipartFile image) throws BadRequestException;

    Player getPlayerByNick(String nick);

    List<Player> searchPlayerByNick(String substring);

    List<Player> getPlayers(int limit);
}

