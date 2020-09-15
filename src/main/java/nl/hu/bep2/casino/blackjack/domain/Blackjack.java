package nl.hu.bep2.casino.blackjack.domain;

public class Blackjack {
    public static void main(String[] args) throws Exception {
        Player player = new Player();
        Dealer dealer = new Dealer(player);
        BlackjackGame blackjackGame = new BlackjackGame(player, dealer);
        blackjackGame.play();
    }
}
