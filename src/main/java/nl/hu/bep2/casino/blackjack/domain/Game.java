package nl.hu.bep2.casino.blackjack.domain;

import java.io.Serializable;

public abstract class Game implements Serializable {

    abstract boolean initializeGame(String username, Long bet) throws InterruptedException;

//    abstract void checkWinOrLose();

    public final void playOneGame(String username, Long bet) throws InterruptedException {
        initializeGame(username, bet);
    }
}
