package nl.hu.bep2.casino.blackjack.domain;

import java.util.Scanner;

public class BlackjackGame {
    private Player player;
    private Dealer dealer;
    private int playerScore = 0;
    private int dealerScore = 0;
    private int chipsBet = 0;

    public BlackjackGame (Player player, Dealer dealer) {
        this.player = player;
        this.dealer = dealer;
    }

    public void play() {
        System.out.println(" _     _            _    _            _    \n" +
                "| |   | |          | |  (_)          | |   \n" +
                "| |__ | | __ _  ___| | ___  __ _  ___| | __\n" +
                "| '_ \\| |/ _` |/ __| |/ / |/ _` |/ __| |/ /\n" +
                "| |_) | | (_| | (__|   <| | (_| | (__|   < \n" +
                "|_.__/|_|\\__,_|\\___|_|\\_\\ |\\__,_|\\___|_|\\_\\\n" +
                "                       _/ |                \n" +
                "                      |__/ ");
        //source: https://ascii.co.uk/art/blackjack

        System.out.println("♣ ♦ ♥ ♠ Deck is getting shuffled ♠ ♥ ♦ ♣");
        this.dealer.shuffleDeck();

        askForBetAmount();

        startingRound();

        while (true) {
            if(playerScore < 22) {
                if(hitOrStand() == 1) {
                    playerHit();
                } else {
                    System.out.println("\n♣ ♦ ♥ ♠ STAND ♠ ♥ ♦ ♣");
                    revealHiddenCard();
                    dealerDrawCards();
                    break;
                }
            } else {
                dealerDrawCards();
                break;
            }
        }

        if (dealerScore >= playerScore && dealerScore < 22) {
            System.out.println("\n♣ ♦ ♥ ♠ You have LOST, say goodbye to your " + chipsBet + " chips ♠ ♥ ♦ ♣");
        } else {
            System.out.println("\n♣ ♦ ♥ ♠ You have WON, say hello to your " + chipsBet + " extra chips ♠ ♥ ♦ ♣");
        }
        System.out.println("Dealer score: " + dealerScore + " Player score: " + playerScore);
    }

    private void playerHit() {
        System.out.println("\n♣ ♦ ♥ ♠ HIT ♠ ♥ ♦ ♣");
        this.dealer.drawCardForPlayer();
        updateCardsScores();
        System.out.println("Your cards: " + this.player.getHand().getCards());
    }

    private void dealerDrawCards() {
        while(dealerScore < 17) {
            this.dealer.drawCardForDealer();
            System.out.println("Dealer has drawn a card");
            updateCardsScores();
            System.out.println("Dealers cards: " + this.dealer.getHand().getCards());
        }
    }

    private void revealHiddenCard() {
        System.out.println("Dealer reveals hidden card");
        System.out.println("Dealers cards: " + this.dealer.getHand().getCards());
    }

    private int hitOrStand() {
        Scanner choiceInput = new Scanner(System.in);
        System.out.print("\nHIT(1) or STAND(2) : ");
        return Integer.parseInt(choiceInput.nextLine());
    }

    private void askForBetAmount () {
        Scanner chipsInput = new Scanner(System.in);
        System.out.print("\nHow many chips do you want to bet? : ");
        chipsBet = Integer.parseInt(chipsInput.nextLine()); // wordt voor nu nog niks mee gedaan
        System.out.println("\nYour bet: " + chipsBet + " Chips");
    }

    private void startingRound() {
        System.out.println("\n♣ ♦ ♥ ♠ Handing out cards ♠ ♥ ♦ ♣");
        this.dealer.startGameHandOutCards();
        System.out.println("\nYour cards: " + this.player.getHand().getCards());
        System.out.println("Dealers cards: " + this.dealer.getHand().getCards().get(0) + " and one non visible card");

        updateCardsScores();
    }

    private void updateCardsScores () {
        int score = 0;
        for (Card card : this.player.getHand().getCards()) {
            score += card.getRank().rank();
        }
        playerScore = score;
        score = 0;

        for (Card card : this.dealer.getHand().getCards()) {
            score += card.getRank().rank();
        }
        dealerScore = score;
    }
}
