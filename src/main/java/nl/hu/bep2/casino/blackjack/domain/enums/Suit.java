package nl.hu.bep2.casino.blackjack.domain.enums;

public enum Suit {
    CLUBS('♣'),
    DIAMONDS('♦'),
    HEARTS('♥'),
    SPADES('♠');

    private char suit;

    Suit(char c) {
        this.suit = c;
    }

    public char suit() {
        return suit;
    }
}
