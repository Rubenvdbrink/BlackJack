package nl.hu.bep2.casino.blackjack.domain;

import org.springframework.stereotype.Component;

@Component
public class Player {
    private Hand hand;
    private Dealer dealer;
    private String username;

    public Player() {
        this.hand = new Hand();
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Hand getHand() {
        return hand;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }
}
