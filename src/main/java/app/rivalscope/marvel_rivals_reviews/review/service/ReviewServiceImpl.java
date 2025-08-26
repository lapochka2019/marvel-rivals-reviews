package app.rivalscope.marvel_rivals_reviews.review.service;

import app.rivalscope.marvel_rivals_reviews.player.model.Player;
import app.rivalscope.marvel_rivals_reviews.player.repository.PlayerRepository;
import app.rivalscope.marvel_rivals_reviews.review.dto.*;
import app.rivalscope.marvel_rivals_reviews.review.model.Review;
import app.rivalscope.marvel_rivals_reviews.review.repository.ReviewRepository;
import app.rivalscope.marvel_rivals_reviews.user.User;
import app.rivalscope.marvel_rivals_reviews.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
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
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public Review create(ReviewCreateDto reviewCreateDto, Long userId) {
        log.info("Попытка получить пользователя с Id {}", userId);
        User user = userRepository.findById(userId).get();
        log.info("Получен пользователь: {}", user);

        log.info("Попытка получить игрока с Id {}", reviewCreateDto.getPlayerId());
        Player player = playerRepository.findById(reviewCreateDto.getPlayerId()).get();
        log.info("Получен игрок: {}", player);

        Review review = reviewMapper.toReview(reviewCreateDto, user, player, LocalDateTime.now());
        log.info("Сформирован отзыв: {}", review);
        return reviewRepository.save(review);

    }

    @Override
    public Review updateImage(Long reviewId, MultipartFile file, Long userId) throws BadRequestException {
        log.info("Обрабатываем изображение");
        String image = saveImage(reviewId, file);

        log.info("Получаем отзыв с id {}", reviewId);

        Review review = reviewRepository.findByIdWithAssociations(reviewId).get();
        log.info("{}",review);

        if(!userId.equals(review.getOwner().getId())){
            throw new IllegalArgumentException("Только владелец отзыва может загрузить изображение");
        }
        review.setImage(image);
        log.info("Обновленный отзыв: {}", review);
        return review;
    }

    @Override
    public List<ReviewResponseWithOwnerDto> getReviewByPlayer(String nick) {
        log.info("Получаем данные игрока с Ником {}", nick);
        Player player = playerRepository.findByNickNameIgnoreCase(nick);
        log.info("Получен игрок: {}", player);

        log.info("Получаем список отзывов об игроке");
        List<Review> playersReviews = reviewRepository.findByPlayerId(player.getId());
        log.info("Получен список отзывов: {}", playersReviews);

        return playersReviews.stream()
                .map(reviewMapper::toReviewResponseWithOwnerDto)
                .toList();
    }

    @Override
    public List<ReviewResponseWithPlayerDto> getReviewByUser(Long userId) {
        log.info("Получаем данные пользователя с Id {}", userId);
        User user = userRepository.findById(userId).get();
        log.info("Получен пользователь: {}", user);

        log.info("Получаем список отзывов пользователя");
        List<Review> ownersReviews = reviewRepository.findByOwnerId(user.getId());
        log.info("Получен список отзывов: {}", ownersReviews);

        return ownersReviews.stream()
                .map(reviewMapper::toReviewResponseWithPlayerDto)
                .toList();
    }

    public String saveImage(Long reviewId, MultipartFile file) throws BadRequestException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Вы не загрузили файл");
        }
        try {
            String fileName = "review_id_" + reviewId + "_image";

            Path uploadDir = Paths.get("src/main/resources/static/uploads/reviews");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/reviews/" + fileName;
        } catch (IOException e) {
            throw new BadRequestException("Во время загрузки изображения произошла ошибка");
        }
    }
}
