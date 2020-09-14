package nl.hu.bep2.casino.blackjack.domain;

public class Dealer {
    private Deck deck;

    public Dealer() {
        this.deck = new Deck();
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }
}
