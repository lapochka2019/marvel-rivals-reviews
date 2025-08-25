package app.rivalscope.marvel_rivals_reviews.review.model;

import app.rivalscope.marvel_rivals_reviews.player.model.Player;
import app.rivalscope.marvel_rivals_reviews.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 512)
    String review;

    @Column()
    LocalDateTime created;

    @Column()
    Integer grade;

    @Column()
    Integer rank;

    @Column(length = 512)
    String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @ToString.Exclude
    User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    @ToString.Exclude
    Player player;
}
