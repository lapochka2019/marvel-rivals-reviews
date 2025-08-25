package app.rivalscope.marvel_rivals_reviews.review.controller;

import app.rivalscope.marvel_rivals_reviews.review.dto.ArrangeData;
import app.rivalscope.marvel_rivals_reviews.review.dto.ReviewCreateDto;
import app.rivalscope.marvel_rivals_reviews.review.dto.ReviewResponseWithOwnerDto;
import app.rivalscope.marvel_rivals_reviews.review.dto.ReviewResponseWithPlayerDto;
import app.rivalscope.marvel_rivals_reviews.review.model.Review;
import app.rivalscope.marvel_rivals_reviews.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Review create(@RequestBody ReviewCreateDto reviewCreateDto, @RequestHeader("X-Mrr-User-Id") Long ownerId) {
        log.info("Запрос на создание отзыва: {} пользователем {}", reviewCreateDto, ownerId);
        return service.create(reviewCreateDto, ownerId);
    }

    @PatchMapping("/{id}")
    public Review update(@PathVariable("id") Long reviewId, @RequestParam("image") MultipartFile image, @RequestHeader("X-Mrr-User-Id") Long ownerId) throws BadRequestException {
        log.info("Запрос на обновление изображения отзыва: {}", reviewId);
        return service.updateImage(reviewId, image, ownerId);
    }

    @GetMapping("/nick/{nick}")
    public List<ReviewResponseWithOwnerDto> getReviewsForPlayer(@PathVariable String nick) {
        log.info("Поиск отзывов об игроке c Ником: {}", nick);
        return service.getReviewByPlayer(nick);
    }

    @GetMapping("/user/{id}")
    public List<ReviewResponseWithPlayerDto> getReviewsForUser(@PathVariable("id") Long userId) {
        log.info("Поиск отзывов пользователя: {}", userId);
        return service.getReviewByUser(userId);
    }

    @GetMapping("/{reviewId}")
    public ArrangeData getMiddleReviewsForPlayer(@PathVariable Long reviewId) {
        log.info("Поиск отзывов об игроке c id: {}", reviewId);
        return service.getMiddleRankByPlayer(reviewId);
    }
}
