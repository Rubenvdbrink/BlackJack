package nl.hu.bep2.casino.blackjack.domain.strategies;

import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;
import nl.hu.bep2.casino.blackjack.domain.enums.GameState;

public class DoubleStrategy implements ActionStrategy {

    @Override
    public boolean doAction(BlackjackGame blackjackGame) {
        if (blackjackGame.getGameState() == GameState.STARTOFGAME) {

            blackjackGame.getBet().setAmount(blackjackGame.getBet().getAmount() * 2);

            blackjackGame.getDealer().drawSingleCard(blackjackGame.getPlayer());

            System.out.println("Your cards: " + blackjackGame.getPlayer().getHand().getCards());

            blackjackGame.setGameState(GameState.PLAYERDOUBLE);

            return true;
        }
        return false;
    }
}
