package nl.hu.bep2.casino.blackjack.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlackJackRepository extends JpaRepository<BlackJack, Long> {
    Optional<BlackJack> findById(Long Id);
}
