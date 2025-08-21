package app.rivalscope.marvel_rivals_reviews.player.service;

import app.rivalscope.marvel_rivals_reviews.exception.ConflictException;
import app.rivalscope.marvel_rivals_reviews.exception.NotFoundException;
import app.rivalscope.marvel_rivals_reviews.player.dto.PlayerCreateDto;
import app.rivalscope.marvel_rivals_reviews.player.dto.PlayerMapper;
import app.rivalscope.marvel_rivals_reviews.player.model.Player;
import app.rivalscope.marvel_rivals_reviews.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    @Override
    public Player create(PlayerCreateDto playerCreateDto) {
        Player player = playerMapper.toPlayer(playerCreateDto);
        log.info("Проверяем, нет ли игрока с ником: {}", player.getNickName());
        checkPlayerExistByNick(player.getNickName());
        player.setCreated(LocalDateTime.now());
        Player newPlayer = playerRepository.save(player);
        log.info("Создан игрок: {}", newPlayer);
        return newPlayer;
    }

    @Override
    public Player updateImage(String nick, MultipartFile file) throws BadRequestException {

        log.info("Обрабатываем изображение");
        String image = saveImage(file, nick);

        log.info("Проверяем, нет ли игрока с ником: {}", nick);
        checkPlayerNotExistByNick(nick);

        Player player = playerRepository.findByNickNameIgnoreCase(nick);
        player.setImage(image);
        log.info("Обновленный игрок: {}", player);
        return player;

    }

    @Override
    public Player getPlayerByNick(String nick) {
        Player player;
        player = playerRepository.findByNickNameIgnoreCase(nick);

        if (player == null) {
            log.info("Игрок с Ником " + nick + " не найден");
            player = create(new PlayerCreateDto(nick));
        }
        return player;
    }

    @Override
    public List<Player> searchPlayerByNick(String substring) {
        List<Player> players = playerRepository.findByNickNameContainingIgnoreCase(substring);

        if (players == null) {
            throw new NotFoundException("Игроков с Ником " + substring + " не найдено");
        }
        log.info("Найден игрок: {}", players);
        return players;
    }

    @Override
    public List<Player> getPlayers(int limit) {
        if(limit==0){
            return playerRepository.findAll();
        }
        Pageable pageable = PageRequest.of(0, limit);
        return playerRepository.findAllByOrderByCreatedDesc(pageable);
    }

    public String saveImage(MultipartFile file, String nick) throws BadRequestException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Вы не загрузили файл");
        }
        try {
            String fileName = nick + "_image";

            Path uploadDir = Paths.get("src/main/resources/static/uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new BadRequestException("Во время загрузки изображения произошла ошибка");
        }
    }

    public void checkPlayerExistByNick(String nick) {
        if (playerRepository.existsByNickNameIgnoreCase(nick)) {
            throw new ConflictException("Игрок с Ником " + nick + " уже существует");
        }
    }

    public void checkPlayerNotExistByNick(String nick) {
        if (!playerRepository.existsByNickNameIgnoreCase(nick)) {
            throw new NotFoundException("Игрок с Ником " + nick + " не найден");
        }
    }
}
