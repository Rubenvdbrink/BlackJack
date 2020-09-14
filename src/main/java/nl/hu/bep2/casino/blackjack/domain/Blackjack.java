package nl.hu.bep2.casino.blackjack.domain;

public class Blackjack {
    public static void main(String[] args) {
        BlackjackGame b1 = new BlackjackGame(new Player(), new Dealer());
        b1.startGame();
    }
}
