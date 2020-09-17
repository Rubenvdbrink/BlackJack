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
        Utils.printWelcome();

        askForBetAmount();

        System.out.println("♣ ♦ ♥ ♠ Deck is getting shuffled ♠ ♥ ♦ ♣");
        this.dealer.shuffleDeck();

        startingRound();

        if(checkBlackJack()) {
            return;
        }

        while (true) {
            if (playerScore < 22) {
                int choice = hitStandOrSurrender();
                if (choice == 1) {
                    System.out.println("\n♣ ♦ ♥ ♠ HIT ♠ ♥ ♦ ♣");
                    playerHit();
                } else if (choice == 2) {
                    System.out.println("\n♣ ♦ ♥ ♠ STAND ♠ ♥ ♦ ♣");
                    revealHiddenCard();
                    dealerDrawCards();
                    winOrLose();
                    break;
                } else if (choice == 3) {
                    System.out.println("\n♣ ♦ ♥ ♠ SURRENDER ♠ ♥ ♦ ♣");
                    revealHiddenCard();
                    surrender();
                    break;
                } else if (choice == 4) {
                    System.out.println("\n♣ ♦ ♥ ♠ DOUBLE ♠ ♥ ♦ ♣");
                    System.out.println("Your bet of " + chipsBet + " chips has been doubled! (" + chipsBet * 2 + ")" );
                    chipsBet *= 2;
                    playerHit();
                    revealHiddenCard();
                    dealerDrawCards();
                    winOrLose();
                    break;
                }
            } else {
                revealHiddenCard();
                winOrLose();
                break;
            }
            }
        System.out.println("♣ ♦ ♥ ♠ Dealer score: " + dealerScore + " Player score: " + playerScore + " ♠ ♥ ♦ ♣");
    }

    private boolean checkBlackJack() {
        if (playerScore == 21 && dealerScore != 21) {
            System.out.println("♣ ♦ ♥ ♠ BlackJack! ♠ ♥ ♦ ♣");
            Utils.printWin();
            System.out.println("\n♣ ♦ ♥ ♠ You have WON, you have won " + chipsBet * 5 + " chips ♠ ♥ ♦ ♣");
            return true;
        }
        return false;
    }

    private void surrender() {
        System.out.println("\n♣ ♦ ♥ ♠ You have SURRENDERED, half of your " + chipsBet + " chips will be returned (" + chipsBet/2 + ") ♠ ♥ ♦ ♣");
    }

    private void winOrLose() {
        if ((dealerScore >= playerScore && dealerScore < 22) || playerScore > 21) { //BUST
            Utils.printLose();
            System.out.println("\n♣ ♦ ♥ ♠ You have LOST, say goodbye to your " + chipsBet + " chips ♠ ♥ ♦ ♣");
        }
        else {
            Utils.printWin();
            System.out.println("\n♣ ♦ ♥ ♠ You have WON, you have won " + chipsBet * 2 + " chips ♠ ♥ ♦ ♣");
        }
    }

    private void playerHit() {
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

    private int hitStandOrSurrender() {
        Scanner choiceInput = new Scanner(System.in);
        System.out.print("\nHIT(1) or STAND(2) or SURRENDER(3) or DOUBLE(4) : ");
        return Integer.parseInt(choiceInput.nextLine());
    }

    //TODO wordt voor nu nog niks mee gedaan
    private void askForBetAmount () {
        Scanner chipsInput = new Scanner(System.in);
        System.out.print("\nHow many chips do you want to bet? : ");
        try {
            chipsBet = Integer.parseInt(chipsInput.nextLine());
        } catch (NumberFormatException NFE) {
            chipsBet = 0;
        }
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
