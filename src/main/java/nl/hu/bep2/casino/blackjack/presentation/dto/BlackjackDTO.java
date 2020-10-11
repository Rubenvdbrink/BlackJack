package nl.hu.bep2.casino.blackjack.presentation.dto;

import nl.hu.bep2.casino.blackjack.domain.Hand;
import nl.hu.bep2.casino.blackjack.domain.enums.GameState;

public class BlackjackDTO {
    public GameState gameState;
    public Hand playerHand;
    public Hand dealerHand;

    public BlackjackDTO(GameState gameState, Hand playerHand, Hand dealerHand) {
        this.gameState = gameState;
        this.playerHand = playerHand;
        this.dealerHand = dealerHand;
    }

}
