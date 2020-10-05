package nl.hu.bep2.casino.blackjack.domain;

import java.io.Serializable;

public abstract class Game implements Serializable {

    abstract void initializeGame(String username) throws InterruptedException;

    abstract void checkWinOrLose();

    public final void playOneGame(String username) throws InterruptedException {
        initializeGame(username);
    }
}
