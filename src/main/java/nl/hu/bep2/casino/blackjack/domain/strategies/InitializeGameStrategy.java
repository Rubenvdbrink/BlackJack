package nl.hu.bep2.casino.blackjack.domain.strategies;

import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;
import nl.hu.bep2.casino.blackjack.domain.Card;
import nl.hu.bep2.casino.blackjack.domain.Person;
import nl.hu.bep2.casino.blackjack.domain.Utils;
import nl.hu.bep2.casino.blackjack.domain.enums.GameState;
import nl.hu.bep2.casino.blackjack.domain.enums.Rank;
import nl.hu.bep2.casino.blackjack.domain.enums.Suit;

import java.util.ArrayList;
import java.util.List;

public class InitializeGameStrategy implements ActionStrategy {

    @Override
    public boolean doAction(BlackjackGame blackjackGame) {
        Utils.printWelcome();

        System.out.println("Welcome, to blackjack!");
        System.out.println("♣ ♦ ♥ ♠ placed a bet of " +  blackjackGame.getBet().getAmount() + " chips ♠ ♥ ♦ ♣");

        System.out.println("♣ ♦ ♥ ♠ Deck is getting shuffled ♠ ♥ ♦ ♣");
        blackjackGame.getDealer().shuffleDeck();

        startingRound(blackjackGame);

//        manipulateCards(new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.ACE, Suit.DIAMONDS), blackjackGame.getPlayer());
//        manipulateCards(new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.ACE, Suit.DIAMONDS), blackjackGame.getDealer());

        checkDoubleAce(blackjackGame.getPlayer());
        checkDoubleAce(blackjackGame.getDealer());
        return checkBlackJack(blackjackGame);
    }

    private void startingRound(BlackjackGame blackjackGame) {
        System.out.println("\n♣ ♦ ♥ ♠ Handing out cards ♠ ♥ ♦ ♣");
        blackjackGame.getDealer().startGameHandOutCards(blackjackGame.getPlayer(), blackjackGame.getDealer());
        System.out.println("\nYour cards: " + blackjackGame.getPlayer().getHand().getCards());
        System.out.println("Dealers cards: " + blackjackGame.getDealer().getHand().getCards().get(0) + " and one non visible card");
    }

    private void checkDoubleAce(Person person) {
        if (person.totalScoreOfCards() == 22) {
            checkForAceAndChangeValue(person);
        }
    }

    private void checkForAceAndChangeValue(Person person) {
        for (Card card : person.getHand().getCards()) {
            if (card.getValue() == 11) {
                card.setValue(1);
                return;
            }
        }
    }

    private boolean checkBlackJack(BlackjackGame blackjackGame) {
        if (blackjackGame.getPlayer().totalScoreOfCards() == 21 && blackjackGame.getDealer().totalScoreOfCards() == 21) {
            blackjackGame.setGameState(GameState.PLAYERPUSH);
            return true;
        } else if (blackjackGame.getPlayer().totalScoreOfCards() == 21) {
            blackjackGame.setGameState(GameState.PLAYERBLACKJACK);
            return true;
        }
        return false;
    }

    //this is just for testing purposes
    private void manipulateCards(Card card1, Card card2, Person person) {
        List<Card> list = new ArrayList<>();
        list.add(card1);
        list.add(card2);
        person.getHand().setCards(list);

        System.out.println("manipulated cards! " + person.getHand().getCards());
    }
}
