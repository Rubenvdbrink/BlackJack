package nl.hu.bep2.casino.blackjack.domain;

import java.io.Serializable;
import java.util.Collections;

public class Dealer implements Serializable, Person {
    private Deck deck;
    private Hand hand;

    public Dealer(Deck deck, Hand hand) {
        this.deck = deck;
        this.hand = hand;
    }

    public void shuffleDeck() {
        Collections.shuffle(this.deck.getCards());
    }

    public void startGameHandOutCards(Player player, Dealer dealer) {
        drawSingleCard(player);
        drawSingleCard(dealer);

        drawSingleCard(player);
        drawSingleCard(dealer);
    }

    public void dealCardsTillScoreSeventeen() {
        while (totalScoreOfCards() < 17) {
            drawSingleCard(this);
        }
    }

    public void drawSingleCard(Person person) {
        person.getHand().getCards().add(this.deck.getFirstCardAndRemoveOutOfDeck());
    }

    @Override
    public int totalScoreOfCards() {
        int score = 0;
        for (Card card : this.hand.getCards()) {
            score += card.getValue();
        }
        return score;
    }

    @Override
    public Hand getHand() {
        return hand;
    }
}
