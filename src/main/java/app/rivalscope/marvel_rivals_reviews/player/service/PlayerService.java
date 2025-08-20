package app.rivalscope.marvel_rivals_reviews.player.service;

import app.rivalscope.marvel_rivals_reviews.player.Player;
import org.apache.coyote.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlayerService {

    Player create(Player player);

    Player updateImage(String name, MultipartFile image) throws BadRequestException;

    Player getPlayerByNick(String nick);

    List<Player> searchPlayerByNick(String substring);

    List<Player> getPlayers(int limit);
}

