package nl.hu.bep2.casino.blackjack.domain;

import nl.hu.bep2.casino.blackjack.domain.enums.GameState;

public class HitStrategy implements ActionStrategy {
    @Override
    public boolean doAction(BlackjackGame blackjackGame) {
        updateCardsScores(blackjackGame);

        if (blackjackGame.getPlayerScore() < 22) {
            blackjackGame.getDealer().drawCardForPlayer();
            updateCardsScores(blackjackGame);

            System.out.println("Your cards: " + blackjackGame.getPlayer().getHand().getCards());

            if (blackjackGame.getPlayerScore() < 22) {
                blackjackGame.setGameState(GameState.PLAYERHIT);
            } else {
                if (!checkForAceAndChangeValue(blackjackGame.getPlayer())) {
                    blackjackGame.setGameState(GameState.PLAYERLOSE);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateCardsScores(BlackjackGame blackjackGame) {
        blackjackGame.setPlayerScore(blackjackGame.getPlayer().totalScoreOfCards());
        blackjackGame.setDealerScore(blackjackGame.getDealer().totalScoreOfCards());
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
