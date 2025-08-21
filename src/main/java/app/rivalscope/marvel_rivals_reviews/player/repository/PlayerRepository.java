package app.rivalscope.marvel_rivals_reviews.player.repository;

import app.rivalscope.marvel_rivals_reviews.player.model.Player;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    boolean existsByNickNameIgnoreCase(String nickName);

    Player findByNickNameIgnoreCase(String nickName);

    //поиск по подстроке
    List<Player> findByNickNameContainingIgnoreCase(String nickName);

    List<Player> findAllByOrderByCreatedDesc(Pageable pageable);


}
