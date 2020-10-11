package nl.hu.bep2.casino.blackjack.domain.strategies;

import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;
import nl.hu.bep2.casino.blackjack.domain.enums.GameState;

public class SurrenderStrategy implements ActionStrategy {

    @Override
    public boolean doAction(BlackjackGame blackjackGame) {
        blackjackGame.setGameState(GameState.PLAYERSURRENDER);
        return true;
    }
}
