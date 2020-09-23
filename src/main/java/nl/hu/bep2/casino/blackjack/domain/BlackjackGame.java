package nl.hu.bep2.casino.blackjack.domain;


import nl.hu.bep2.casino.blackjack.domain.enums.GameState;
import nl.hu.bep2.casino.blackjack.domain.enums.Rank;
import nl.hu.bep2.casino.blackjack.domain.enums.Suit;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class BlackjackGame extends Game {
    private Player player;
    private Dealer dealer;
    private int playerScore = 0;
    private int dealerScore = 0;
    private int chipsBet = 0; //TODO wordt voor nu niks meer mee gedaan
    private Bet bet;
    private GameState gameState;

    public BlackjackGame(Player player, Dealer dealer) {
        this.player = player;
        this.dealer = dealer;
    }

    public void initializeGame() {
        Utils.printWelcome();

        askForBetAmount();

        System.out.println("♣ ♦ ♥ ♠ Deck is getting shuffled ♠ ♥ ♦ ♣");
        this.dealer.shuffleDeck();

        startingRound();
        updateCardsScores();

        if (checkBlackJack()) {
            return;
        }

        while (true) {
            if (playerScore < 22) {
                int choice = hitStandOrSurrender();
                if (choice == 1) {
                    Utils.printHit();

                    this.dealer.drawCardForPlayer();
                    System.out.println("Your cards: " + this.player.getHand().getCards());

                    updateCardsScores();
                } else if (choice == 2) {
                    Utils.printStand();
                    revealHiddenCard();

                    dealer.playerStands();
                    System.out.println("Dealer has drawn card(s)");
                    System.out.println("Dealers cards: " + this.dealer.getHand().getCards());

                    updateCardsScores();
                    checkWinOrLose();
                    break;
                } else if (choice == 3) {
                    Utils.printSurrender();
                    revealHiddenCard();
                    System.out.println("\n♣ ♦ ♥ ♠ You have SURRENDERED, half of your " + chipsBet + " chips will be returned (" + chipsBet / 2 + ") ♠ ♥ ♦ ♣");
                    this.player.surrender();
                    break;
                } else {
                    Utils.printDouble();
                    System.out.println("Your bet of " + chipsBet + " chips has been doubled! (" + chipsBet * 2 + ")");
                    chipsBet *= 2;
                    this.dealer.drawCardForPlayer();
                    revealHiddenCard();

                    dealer.playerStands();
                    System.out.println("Dealer has drawn card(s)");
                    System.out.println("Dealers cards: " + this.dealer.getHand().getCards());

                    updateCardsScores();
                    checkWinOrLose();
                    break;
                }
            } else {
                revealHiddenCard();
                checkWinOrLose();
                break;
            }
        }
        System.out.println("♣ ♦ ♥ ♠ Dealer score: " + dealerScore + " Player score: " + playerScore + " ♠ ♥ ♦ ♣");
    }

    public boolean checkBlackJack() {
        if (playerScore == 21 && dealerScore != 21) {
            System.out.println("♣ ♦ ♥ ♠ BlackJack! ♠ ♥ ♦ ♣");
            Utils.printWin();
            System.out.println("\n♣ ♦ ♥ ♠ You have WON, you have won " + bet.getAmount() * 5 + " chips ♠ ♥ ♦ ♣");
            return true;
        }
        return false;
    }

    public void checkWinOrLose() {
        if ((dealerScore >= playerScore && dealerScore < 22) || playerScore > 21) { //BUST
            Utils.printLose();
            System.out.println("\n♣ ♦ ♥ ♠ You have LOST, say goodbye to your " + chipsBet + " chips ♠ ♥ ♦ ♣");
        } else {
            Utils.printWin();
            System.out.println("\n♣ ♦ ♥ ♠ You have WON, you have won " + chipsBet * 2 + " chips ♠ ♥ ♦ ♣");
        }
    }

    public void revealHiddenCard() {
        System.out.println("\nDealer reveals hidden card");
        System.out.println("Dealers cards: " + this.dealer.getHand().getCards());
    }

    //TODO
    //wordt voor nu niks meer mee gedaan
    private int hitStandOrSurrender() {
        Scanner choiceInput = new Scanner(System.in);
        System.out.print("\nHIT(1) or STAND(2) or SURRENDER(3) or DOUBLE(4) : ");
        return Integer.parseInt(choiceInput.nextLine());
    }

    //TODO
    //wordt voor nu niks meer mee gedaan
    private void askForBetAmount() {
        Scanner chipsInput = new Scanner(System.in);
        System.out.print("\nHow many chips do you want to bet? : ");
        try {
            chipsBet = Integer.parseInt(chipsInput.nextLine());
        } catch (NumberFormatException NFE) {
            askForBetAmount();
        }
        System.out.println("\nYour bet: " + chipsBet + " Chips");
    }

    public void startingRound() {
        System.out.println("\n♣ ♦ ♥ ♠ Handing out cards ♠ ♥ ♦ ♣");
        this.dealer.startGameHandOutCards();
        System.out.println("\nYour cards: " + this.player.getHand().getCards());
        System.out.println("Dealers cards: " + this.dealer.getHand().getCards().get(0) + " and one non visible card");
        updateCardsScores();
    }

    public void updateCardsScores() {
        playerScore = player.totalScoreOfCards();
        dealerScore = dealer.totalScoreOfCards();
    }

    //TODO
    //remove before version 1.0, this is just for testing purposes
    public void fakeBlackJackForPlayer() {
        List<Card> list = new ArrayList<>();
        list.add(new Card(Rank.ACE, Suit.DIAMONDS));
        list.add(new Card(Rank.TEN, Suit.DIAMONDS));
        this.player.getHand().setCards(list);

        updateCardsScores();

        System.out.println("fake blackjack for player! " + this.player.getHand().getCards());
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public Bet getBet() {
        return bet;
    }

    public void setBet(Bet bet) {
        this.bet = bet;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getDealerScore() {
        return dealerScore;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
