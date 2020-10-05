package nl.hu.bep2.casino.blackjack.domain;

import nl.hu.bep2.casino.blackjack.domain.enums.Rank;
import nl.hu.bep2.casino.blackjack.domain.enums.Suit;

import java.io.Serializable;

public class Card implements Serializable {
    private Rank rank;
    private Suit suit;
    private int value;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.value = rank.rank();
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() { return suit; }

    public int getValue() { return value; }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return " ["
                + rank
                + "/"
                + suit.suit()
                + "]("
                + value
                + ") ";
    }
}
