package nl.hu.bep2.casino.blackjack.domain;

public abstract class Game {

    abstract void initializeGame(String username) throws InterruptedException;

    abstract void checkWinOrLose();

    public final void playOneGame(String username) throws InterruptedException {
        initializeGame(username);
    }
}
