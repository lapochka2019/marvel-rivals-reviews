package app.rivalscope.marvel_rivals_reviews.review.repository;

import app.rivalscope.marvel_rivals_reviews.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.player " +
            "WHERE r.owner.id = :id")
    List<Review> findByOwnerId(@Param("id") Long id);

    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.owner " +
            "WHERE r.player.id = :id")
    List<Review> findByPlayerId(@Param("id") Long id);

    @Query("SELECT AVG(r.rank) FROM Review r " +
            "WHERE r.player.id = :playerId " +
            "AND r.created >= :since")
    Optional<Double> findAverageRankLast30DaysByPlayerId(
            @Param("playerId") Long playerId,
            @Param("since") LocalDateTime since
    );

    @Query("SELECT AVG(r.grade) FROM Review r " +
            "WHERE r.player.id = :playerId " +
            "AND r.created >= :since")
    Optional<Double> findAverageGradeLast30DaysByPlayerId(
            @Param("playerId") Long playerId,
            @Param("since") LocalDateTime since
    );

    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.player " +
            "JOIN FETCH r.owner " +
            "WHERE r.id = :id")
    Optional<Review> findByIdWithAssociations(@Param("id") Long id);
}
