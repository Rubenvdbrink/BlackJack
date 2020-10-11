package nl.hu.bep2.casino.blackjack.domain.strategies;

import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;
import nl.hu.bep2.casino.blackjack.domain.Card;
import nl.hu.bep2.casino.blackjack.domain.Person;
import nl.hu.bep2.casino.blackjack.domain.enums.GameState;

public class HitStrategy implements ActionStrategy {

    @Override
    public boolean doAction(BlackjackGame blackjackGame) {

        if (blackjackGame.getPlayer().totalScoreOfCards() < 21) {
            blackjackGame.getDealer().drawSingleCard(blackjackGame.getPlayer());

            System.out.println("Your cards: " + blackjackGame.getPlayer().getHand().getCards());

            if(blackjackGame.getPlayer().totalScoreOfCards() == 21) {
                blackjackGame.setGameState(GameState.PLAYERHIT);
                return false;
            }

            if (blackjackGame.getPlayer().totalScoreOfCards() > 21) {
                checkForAceAndChangeValue(blackjackGame.getPlayer());
            }

            if (blackjackGame.getPlayer().totalScoreOfCards() < 22) {
                blackjackGame.setGameState(GameState.PLAYERHIT);
//                checkForAceAndChangeValue(blackjackGame.getPlayer());

//                return blackjackGame.getPlayer().totalScoreOfCards() != 21;
                return true;
            } else {
//                if (!checkForAceAndChangeValue(blackjackGame.getPlayer())) {
                    blackjackGame.setGameState(GameState.PLAYERLOSE);
                    return false;
//                }
            }
        } return false;

//            if (blackjackGame.getPlayer().totalScoreOfCards() < 22) {
//                blackjackGame.setGameState(GameState.PLAYERHIT);
//            } else {
//                if (!checkForAceAndChangeValue(blackjackGame.getPlayer())) {
//                    blackjackGame.setGameState(GameState.PLAYERLOSE);
//                }
//            }
//            return true;
//        } else {
//            return false;
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
}
