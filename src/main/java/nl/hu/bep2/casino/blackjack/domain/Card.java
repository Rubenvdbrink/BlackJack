package nl.hu.bep2.casino.blackjack.domain;

import nl.hu.bep2.casino.blackjack.domain.enums.Rank;
import nl.hu.bep2.casino.blackjack.domain.enums.Suit;

public class Card {
    private Rank rank;
    private Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    //♣ ♦ ♥ ♠
    @Override
    public String toString() {
        char suitUnicode;
        if (suit.equals(Suit.CLUBS)) {
            suitUnicode = '♣';
        }
        else if (suit.equals(Suit.DIAMONDS)) {
            suitUnicode = '♦';
        }
        else if (suit.equals(Suit.HEARTS)) {
            suitUnicode = '♥';
        }

        else {
            suitUnicode = '♠';
        }

        return " [" + rank + "/" + suitUnicode + "](" + rank.rank() + ") ";
    }
}
