package nl.hu.bep2.casino.blackjack.domain;

public class BlackjackGame {
    private Player player;
    private Dealer dealer;

    public BlackjackGame (Player player, Dealer dealer) {
        this.player = player;
        this.dealer = dealer;
    }

    public void startGame() {
//        System.out.print("Card Deck: ");
//        for (Card card : game.dealer.getDeck().getCards()) {
//            System.out.print(card);
//        }
        System.out.println("♣ ♦ ♥ ♠ Deck is getting shuffled... ♠ ♥ ♦ ♣");
        this.dealer.getDeck().shuffleDeck();
        System.out.println("♣ ♦ ♥ ♠ Deck is shuffled ♠ ♥ ♦ ♣");
    }
}
