package nl.hu.bep2.casino.blackjack.domain;

import nl.hu.bep2.casino.blackjack.domain.enums.Rank;
import nl.hu.bep2.casino.blackjack.domain.enums.Suit;

import java.io.Serializable;

public class Card implements Serializable {
    private Rank rank;
    private Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return " ["
                + rank
                + "/"
                + suit.suit()
                + "]("
                + rank.rank()
                + ") ";
    }
}
