package nl.hu.bep2.casino.blackjack.domain.strategies;

import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;

public interface ActionStrategy {
    boolean doAction(BlackjackGame blackjackGame);
}
