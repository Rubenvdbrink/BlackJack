package nl.hu.bep2.casino.blackjack.application;

import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;

public interface BlackjackGameFactory {
    BlackjackGame create(Long bet);
}
