package nl.hu.bep2.casino.blackjack.domain;

import nl.hu.bep2.casino.blackjack.domain.enums.GameState;

import java.util.ArrayList;
import java.util.List;

public class InitializeGameStrategy implements ActionStrategy {
    @Override
    public boolean doAction(BlackjackGame blackjackGame) {
        Utils.printWelcome();
//        blackjackGame.setBet(new Bet(bet));

        System.out.println("Welcome, to blackjack!");
        System.out.println("♣ ♦ ♥ ♠ placed a bet of " +  blackjackGame.getBet().getAmount() + " chips ♠ ♥ ♦ ♣");

        System.out.println("♣ ♦ ♥ ♠ Deck is getting shuffled ♠ ♥ ♦ ♣");
        blackjackGame.getDealer().shuffleDeck();

        startingRound(blackjackGame);

//        manipulateCards(new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.ACE, Suit.DIAMONDS), player, blackjackGame);
//        manipulateCards(new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.ACE, Suit.DIAMONDS), dealer, blackjackGame);

        checkDoubleAce(blackjackGame.getPlayer());
        checkDoubleAce(blackjackGame.getDealer());
        return checkBlackJack(blackjackGame);
    }

    @Override
    public void updateCardsScores(BlackjackGame blackjackGame) {
        blackjackGame.setPlayerScore(blackjackGame.getPlayer().totalScoreOfCards());
        blackjackGame.setDealerScore(blackjackGame.getDealer().totalScoreOfCards());
    }

    private void startingRound(BlackjackGame blackjackGame) {
        System.out.println("\n♣ ♦ ♥ ♠ Handing out cards ♠ ♥ ♦ ♣");
        blackjackGame.getDealer().startGameHandOutCards();
        System.out.println("\nYour cards: " + blackjackGame.getPlayer().getHand().getCards());
        System.out.println("Dealers cards: " + blackjackGame.getDealer().getHand().getCards().get(0) + " and one non visible card");
        updateCardsScores(blackjackGame);
    }

    private void checkDoubleAce(Person person) {
        if (person.totalScoreOfCards() == 22) {
            checkForAceAndChangeValue(person);
        }
    }

    private boolean checkForAceAndChangeValue(Person person) {
        for (Card card : person.getHand().getCards()) {
            if (card.getValue() == 11) {
                card.setValue(1);
                return true;
            }
        }
        return false;
    }

    private boolean checkBlackJack(BlackjackGame blackjackGame) {
        if (blackjackGame.getPlayerScore() == 21 && blackjackGame.getDealerScore() == 21) {
            blackjackGame.setGameState(GameState.PLAYERPUSH);
            return true;
        } else if (blackjackGame.getPlayerScore() == 21) {
            blackjackGame.setGameState(GameState.PLAYERBLACKJACK);
            return true;
        }
        return false;
    }

    //this is just for testing purposes
    private void manipulateCards(Card card1, Card card2, Person person, BlackjackGame blackjackGame) {
        List<Card> list = new ArrayList<>();
        list.add(card1);
        list.add(card2);
        person.getHand().setCards(list);

        updateCardsScores(blackjackGame);

        System.out.println("manipulated cards! " + person.getHand().getCards());
    }
}
