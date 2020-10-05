package nl.hu.bep2.casino.blackjack.data;

import nl.hu.bep2.casino.security.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlackjackRepository extends JpaRepository<Blackjack, Long> {
    Optional<Blackjack> findById(Long Id);

    //get's the last game of specified user
    Optional<Blackjack> findTopByUserAndGameDoneOrderByIdDesc(User user, Boolean gameDone);
}
