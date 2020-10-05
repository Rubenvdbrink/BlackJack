package nl.hu.bep2.casino.blackjack.domain;

import nl.hu.bep2.casino.blackjack.domain.enums.Rank;
import nl.hu.bep2.casino.blackjack.domain.enums.Suit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Deck implements Serializable {
    private List<Card> cards = new ArrayList<>();

    public Deck() {
        fillDeck();
    }

    private void fillDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public Card getFirstCardAndRemoveOutOfDeck() {
        Card firstCard = this.cards.get(0); //gets first card out of deck
        this.cards.remove(0);  //and removes it from the deck
        return firstCard;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
