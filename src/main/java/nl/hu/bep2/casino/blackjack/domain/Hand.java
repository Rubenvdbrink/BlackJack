package nl.hu.bep2.casino.blackjack.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Hand {
    private List<Card> cards = new ArrayList<>();

    public Hand() {}

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
