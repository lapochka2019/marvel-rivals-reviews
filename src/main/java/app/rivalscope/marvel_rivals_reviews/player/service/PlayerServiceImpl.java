package app.rivalscope.marvel_rivals_reviews.player.service;

import app.rivalscope.marvel_rivals_reviews.exception.ConflictException;
import app.rivalscope.marvel_rivals_reviews.exception.NotFoundException;
import app.rivalscope.marvel_rivals_reviews.player.dto.PlayerCreateDto;
import app.rivalscope.marvel_rivals_reviews.player.dto.PlayerDto;
import app.rivalscope.marvel_rivals_reviews.player.dto.PlayerMapper;
import app.rivalscope.marvel_rivals_reviews.player.model.Player;
import app.rivalscope.marvel_rivals_reviews.player.repository.PlayerRepository;
import app.rivalscope.marvel_rivals_reviews.review.dto.ArrangeData;
import app.rivalscope.marvel_rivals_reviews.review.repository.ReviewRepository;
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
    private final ReviewRepository reviewRepository;
    private final PlayerMapper playerMapper;

    @Override
    public PlayerDto create(PlayerCreateDto playerCreateDto) {
        Player player = playerMapper.toPlayer(playerCreateDto);
        log.info("Проверяем, нет ли игрока с ником: {}", player.getNickName());
        checkPlayerExistByNick(player.getNickName());
        player.setCreated(LocalDateTime.now());
        Player newPlayer = playerRepository.save(player);
        log.info("Создан игрок: {}", newPlayer);
        return playerMapper.toPlayerDto(newPlayer, new ArrangeData(0.0, 0.0));
    }

    @Override
    public PlayerDto updateImage(String nick, MultipartFile file) throws BadRequestException {

        log.info("Обрабатываем изображение");
        String image = saveImage(nick, file);

        log.info("Проверяем, есть ли игрок с ником: {}", nick);
        checkPlayerNotExistByNick(nick);

        Player player = playerRepository.findByNickNameIgnoreCase(nick);
        player.setImage(image);
        log.info("Обновленный игрок: {}", player);

        ArrangeData arrangeData = getMiddleRankByPlayer(player.getId());
        return playerMapper.toPlayerDto(player, arrangeData);
    }

    @Override
    public PlayerDto getPlayerByNick(String nick) {
        Player player;
        player = playerRepository.findByNickNameIgnoreCase(nick);

        if (player == null) {
            log.info("Игрок с Ником " + nick + " не найден");
            return create(new PlayerCreateDto(nick));
        }

        ArrangeData arrangeData = getMiddleRankByPlayer(player.getId());
        return playerMapper.toPlayerDto(player, arrangeData);
    }

    @Override
    public List<Player> searchPlayerByNick(String substring) {
        List<Player> players = playerRepository.findByNickNameContainingIgnoreCase(substring);

        if (players == null) {
            throw new NotFoundException("Игроков с Ником " + substring + " не найдено");
        }
        log.info("Найдены игроки: {}", players);
        return players;
    }

    @Override
    public List<Player> getPlayers(int limit) {
        if (limit == 0) {
            return playerRepository.findAll();
        }
        Pageable pageable = PageRequest.of(0, limit);
        return playerRepository.findAllByOrderByCreatedDesc(pageable);
    }

    public String saveImage(String nick, MultipartFile file) throws BadRequestException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Вы не загрузили файл");
        }
        try {
            String fileName = nick + "_image";

            Path uploadDir = Paths.get("src/main/resources/static/uploads/players");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/players/" + fileName;
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

    public ArrangeData getMiddleRankByPlayer(Long playerId) {
        Double avgRank = reviewRepository.findAverageRankLast30DaysByPlayerId(playerId, LocalDateTime.now().minusDays(30)).orElse(Double.valueOf(0));
        Double avgGrade = reviewRepository.findAverageGradeLast30DaysByPlayerId(playerId, LocalDateTime.now().minusDays(30)).orElse(Double.valueOf(0));

        return new ArrangeData(avgRank, avgGrade);
    }
}
