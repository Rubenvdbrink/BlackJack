package nl.hu.bep2.casino.blackjack.domain;

public class Player {
    private Hand hand;
    private Dealer dealer;

    public Player() {
        this.hand = new Hand();
    }

    public Hand getHand() {
        return hand;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }
}
