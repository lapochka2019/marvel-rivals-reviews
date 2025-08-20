package app.rivalscope.marvel_rivals_reviews.player.service;


import app.rivalscope.marvel_rivals_reviews.player.Player;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Player create(@RequestBody Player player) {
        log.info("Запрос на создание игрока: {}", player);
        return service.create(player);
    }

    @PatchMapping("/{nick}")
    public Player update(@PathVariable String nick, @RequestParam("image") MultipartFile image) throws BadRequestException {
        log.info("Запрос на обновление изображения игрока c Ником: {}", nick);
        return service.updateImage(nick, image);
    }

    @GetMapping("/nick/{nick}")
    public Player getPlayerByNick(@PathVariable String nick) {
        log.info("Запрос на получение игрока c Ником: {}", nick);
        return service.getPlayerByNick(nick);
    }

    @GetMapping("/search")
    public List<Player> searchPlayerByNick(@RequestParam String nick) {
        log.info("Поиск игрока игрока c Ником: {}", nick);
        return service.searchPlayerByNick(nick);
    }

    @GetMapping()
    public List<Player> getPlayers(@RequestParam(defaultValue = "10") int limit) {
        log.info("Получить список из {} игроков (сортировка desc о дате создания)", limit);
        return service.getPlayers(limit);
    }
}
