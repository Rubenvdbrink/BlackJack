package nl.hu.bep2.casino.blackjack.domain;

import nl.hu.bep2.casino.blackjack.domain.enums.GameState;

import java.io.Serializable;

public class BlackjackGame implements Serializable {
    private Player player;
    private Dealer dealer;
    private Bet bet;
    private GameState gameState = GameState.STARTOFGAME;

    public BlackjackGame(Player player, Dealer dealer, Bet bet) {
        this.player = player;
        this.dealer = dealer;
        this.bet = bet;
    }

    public Player getPlayer() {
        return player;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Bet getBet() {
        return bet;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
