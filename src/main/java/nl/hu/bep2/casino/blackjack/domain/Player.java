package nl.hu.bep2.casino.blackjack.domain;

import java.io.Serializable;

public class Player implements Serializable, Person {
    private Hand hand;

//    //ToDo fix
//    private Dealer dealer;

    public Player(Hand hand) {
        this.hand = hand;
    }

    public void surrender() {}

    public int totalScoreOfCards() {
        int score = 0;
        if(!this.hand.getCards().isEmpty()) {
            for (Card card : this.hand.getCards()) {
                score += card.getValue();
            }
        }
        return score;
    }

    public Hand getHand() {
        return hand;
    }

    //ToDo fix
//    public void setDealer(Dealer dealer) {
//        this.dealer = dealer;
//    }
}
