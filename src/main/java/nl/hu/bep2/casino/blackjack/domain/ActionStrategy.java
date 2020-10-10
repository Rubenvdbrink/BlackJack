package nl.hu.bep2.casino.blackjack.domain;

public interface ActionStrategy {
    boolean doAction(BlackjackGame blackjackGame);
    void updateCardsScores(BlackjackGame blackjackGame);
}
