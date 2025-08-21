package app.rivalscope.marvel_rivals_reviews.player.model;

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
@Table(name = "players")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "nick_name", nullable = false, unique = true)
    String nickName;

    @Column()
    Integer owner;

    @Column()
    LocalDateTime created;

    @Column(length = 511)
    String image;

}
