package nl.hu.bep2.casino.blackjack.domain;

import org.springframework.stereotype.Component;

import java.io.Serializable;

public class Player implements Serializable {
    private Hand hand;
    private Dealer dealer;

    public Player(Hand hand) {
        this.hand = hand;
    }

    public void surrender() {}

    public int totalScoreOfCards() {
        int score = 0;
        if(!this.hand.getCards().isEmpty()) {
            for (Card card : this.hand.getCards()) {
                score += card.getRank().rank();
            }
        }
        return score;
    }

    public Hand getHand() {
        return hand;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }
}
