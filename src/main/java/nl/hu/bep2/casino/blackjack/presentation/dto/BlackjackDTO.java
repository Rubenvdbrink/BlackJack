package nl.hu.bep2.casino.blackjack.presentation.dto;

import nl.hu.bep2.casino.blackjack.domain.Hand;
import nl.hu.bep2.casino.blackjack.domain.enums.GameState;

public class BlackjackDTO {
    public Hand playerHand;
    public Hand dealerHand;
    public GameState gameState;

    public BlackjackDTO(Hand playerHand, Hand dealerHand, GameState gameState) {
        this.playerHand = playerHand;
        this.dealerHand = dealerHand;
        this.gameState = gameState;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public GameState getGameState() {
        return gameState;
    }
}
