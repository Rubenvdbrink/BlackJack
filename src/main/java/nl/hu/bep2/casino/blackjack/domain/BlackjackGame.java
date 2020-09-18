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
        updateCardsScores();

        if(checkBlackJack()) {
            return;
        }

        while (true) {
            if (playerScore < 22) {
                int choice = hitStandOrSurrender();
                if (choice == 1) {
                    System.out.println("\n♣ ♦ ♥ ♠ HIT ♠ ♥ ♦ ♣");

                    this.dealer.drawCardForPlayer();
                    System.out.println("Your cards: " + this.player.getHand().getCards());

                    updateCardsScores();
                } else if (choice == 2) {
                    System.out.println("\n♣ ♦ ♥ ♠ STAND ♠ ♥ ♦ ♣");
                    revealHiddenCard();

                    dealer.playerStands();
                    System.out.println("Dealer has drawn card(s)");
                    System.out.println("Dealers cards: " + this.dealer.getHand().getCards());

                    updateCardsScores();
                    winOrLose();
                    break;
                } else if (choice == 3) {
                    System.out.println("\n♣ ♦ ♥ ♠ SURRENDER ♠ ♥ ♦ ♣");
                    revealHiddenCard();
                    System.out.println("\n♣ ♦ ♥ ♠ You have SURRENDERED, half of your " + chipsBet + " chips will be returned (" + chipsBet/2 + ") ♠ ♥ ♦ ♣");
                    this.player.surrender();
                    break;
                } else {
                    System.out.println("\n♣ ♦ ♥ ♠ DOUBLE ♠ ♥ ♦ ♣");
                    System.out.println("Your bet of " + chipsBet + " chips has been doubled! (" + chipsBet * 2 + ")" );
                    chipsBet *= 2;
                    this.dealer.drawCardForPlayer();
                    revealHiddenCard();

                    dealer.playerStands();
                    System.out.println("Dealer has drawn card(s)");
                    System.out.println("Dealers cards: " + this.dealer.getHand().getCards());

                    updateCardsScores();
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

    private void revealHiddenCard() {
        System.out.println("Dealer reveals hidden card");
        System.out.println("Dealers cards: " + this.dealer.getHand().getCards());
    }

    private int hitStandOrSurrender() {
        Scanner choiceInput = new Scanner(System.in);
        System.out.print("\nHIT(1) or STAND(2) or SURRENDER(3) or DOUBLE(4) : ");
        return Integer.parseInt(choiceInput.nextLine());
    }

    //TODO
    //wordt voor nu nog niks mee gedaan
    private void askForBetAmount () {
        Scanner chipsInput = new Scanner(System.in);
        System.out.print("\nHow many chips do you want to bet? : ");
        try {
            chipsBet = Integer.parseInt(chipsInput.nextLine());
        } catch (NumberFormatException NFE) {
            askForBetAmount();
        }
        System.out.println("\nYour bet: " + chipsBet + " Chips");
    }

    private void startingRound() {
        System.out.println("\n♣ ♦ ♥ ♠ Handing out cards ♠ ♥ ♦ ♣");
        this.dealer.startGameHandOutCards();
        System.out.println("\nYour cards: " + this.player.getHand().getCards());
        System.out.println("Dealers cards: " + this.dealer.getHand().getCards().get(0) + " and one non visible card");
    }

    private void updateCardsScores () {
        playerScore = player.totalScoreOfCards();
        dealerScore = dealer.totalScoreOfCards();
    }
}
