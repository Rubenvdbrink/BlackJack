package nl.hu.bep2.casino.blackjack.domain.strategies;

import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;
import nl.hu.bep2.casino.blackjack.domain.Card;
import nl.hu.bep2.casino.blackjack.domain.Dealer;
import nl.hu.bep2.casino.blackjack.domain.Person;
import nl.hu.bep2.casino.blackjack.domain.enums.GameState;

public class StandOrDealersTurnStrategy implements ActionStrategy {

    @Override
    public boolean doAction(BlackjackGame blackjackGame) {
        revealHiddenCard(blackjackGame.getDealer());
        blackjackGame.getDealer().dealCardsTillScoreSeventeen();

        while (blackjackGame.getDealer().totalScoreOfCards() > 21) {
            if (checkForAceAndChangeValue(blackjackGame.getDealer())) {
                blackjackGame.getDealer().dealCardsTillScoreSeventeen();
            } else {
                break;
            }
        }

        System.out.println("Dealer has drawn card(s)");
        System.out.println("Dealers cards: " + blackjackGame.getDealer().getHand().getCards());

        revealHiddenCard(blackjackGame.getDealer());

        if (blackjackGame.getGameState() == GameState.PLAYERSURRENDER || blackjackGame.getGameState() == GameState.PLAYERPUSH) {
            return false;
        }

        if (checkLosingConditions(blackjackGame)) {
            blackjackGame.setGameState(GameState.PLAYERLOSE);
        } else {
            if (checkWinningConditions(blackjackGame)) {
                blackjackGame.setGameState(GameState.PLAYERNORMALWIN);
            }
        }
        System.out.println("♣ ♦ ♥ ♠ Dealer score: " + blackjackGame.getDealer().totalScoreOfCards() + " Player score: " + blackjackGame.getPlayer().totalScoreOfCards() + " Game state: " + blackjackGame.getGameState() + " ♠ ♥ ♦ ♣");
        return true;
    }

    //only needed for console output
    private void revealHiddenCard(Dealer dealer) {
        System.out.println("\nDealer reveals hidden card");
        System.out.println("Dealers cards: " + dealer.getHand().getCards());
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

    private boolean checkLosingConditions(BlackjackGame blackjackGame) {
        return blackjackGame.getDealer().totalScoreOfCards()
                >= blackjackGame.getPlayer().totalScoreOfCards()
                && blackjackGame.getDealer().totalScoreOfCards() < 22
                || blackjackGame.getPlayer().totalScoreOfCards() > 21;
    }

    private boolean checkWinningConditions(BlackjackGame blackjackGame) {
        return blackjackGame.getGameState()
                != GameState.PLAYERDOUBLE
                && blackjackGame.getGameState()
                != GameState.PLAYERBLACKJACK
                && blackjackGame.getGameState()
                != GameState.PLAYERPUSH;
    }
}
