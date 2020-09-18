package nl.hu.bep2.casino.blackjack.domain;

public class Player {
    private Hand hand;
    private Dealer dealer;

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

    public Hand getHand() {
        return hand;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }
}
