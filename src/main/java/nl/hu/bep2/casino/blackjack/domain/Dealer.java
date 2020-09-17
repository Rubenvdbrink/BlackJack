package nl.hu.bep2.casino.blackjack.domain;

import java.util.Collections;

public class Dealer {
    private Deck deck;
    private Hand hand;
    private Player player;

    public Dealer() {
        this.deck = new Deck();
        this.hand = new Hand();
    }

    public void shuffleDeck() {
        Collections.shuffle(this.deck.getCards());
    }

    public void startGameHandOutCards() {
        drawCardForPlayer();
        drawCardForDealer();

        drawCardForPlayer();
        drawCardForDealer();
    }

    public void drawCardForDealer() {
        this.hand.getCards().add(this.deck.getFirstCardAndRemoveOutOfDeck());
    }

    public void drawCardForPlayer() {
        this.player.getHand().getCards().add(this.deck.getFirstCardAndRemoveOutOfDeck());
    }

    public Deck getDeck() {
        return deck;
    }

    public Hand getHand() {
        return hand;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
