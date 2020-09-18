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

    public void playerStands() {
        while(totalScoreOfCards() < 17) {
            drawCardForDealer();
        }
    }

    public void drawCardForDealer() {
        this.hand.getCards().add(this.deck.getFirstCardAndRemoveOutOfDeck());
    }

    public void drawCardForPlayer() { this.player.getHand().getCards().add(this.deck.getFirstCardAndRemoveOutOfDeck()); }

    public int totalScoreOfCards() {
        int score = 0;
        if(!this.hand.getCards().isEmpty()) {
            for (Card card : this.hand.getCards()) {
                score += card.getRank().rank();
            }
        }
        return score;
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
