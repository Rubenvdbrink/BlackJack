package nl.hu.bep2.casino.blackjack.domain;

public abstract class Game {

    abstract void initializeGame();

    abstract void checkWinOrLose();

    public final void playOneGame() {
        initializeGame();
    }
}
