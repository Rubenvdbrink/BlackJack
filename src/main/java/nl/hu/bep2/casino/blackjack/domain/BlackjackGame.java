package nl.hu.bep2.casino.blackjack.domain;


import nl.hu.bep2.casino.blackjack.domain.enums.GameState;
import nl.hu.bep2.casino.blackjack.domain.enums.Rank;
import nl.hu.bep2.casino.blackjack.domain.enums.Suit;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BlackjackGame extends Game {
    private Player player;
    private Dealer dealer;
    private int playerScore = 0;
    private int dealerScore = 0;
    private Bet bet;
    private GameState gameState;

    public BlackjackGame(Player player, Dealer dealer) {
        this.player = player;
        this.dealer = dealer;
    }

    @Override
    public void initializeGame(String username) {
        player.setUsername(username);
        System.out.println("Welcome, " + player.getUsername() + " to blackjack!");
        System.out.println("♣ ♦ ♥ ♠ " + username + " has placed a bet of " + bet.getAmount() + " chips ♠ ♥ ♦ ♣");

        System.out.println("♣ ♦ ♥ ♠ Deck is getting shuffled ♠ ♥ ♦ ♣");
        dealer.shuffleDeck();
        gameState = GameState.STARTOFGAME;
        startingRound();

        fakeBlackJackForPlayer();
        if (checkBlackJack()) {
            return;
        }
//
////        gameLoop();
//
//        if (checkDouble()) {
//            return;
//        }
//        checkWinOrLose();
    }

//    public void gameLoop() throws InterruptedException {
//        System.out.println("What is your next move? HIT, STAND, DOUBLE OR SURRENDER");
//          while (true) {
//            if (gameState == GameState.WAITFORPLAYERACTION) {
//                Thread.sleep(1000);
//            }
//            else if (gameState == GameState.PLAYERHIT) {
//                if (playerScore < 22) {
//                    System.out.println("What is your next move? HIT, STAND, DOUBLE OR SURRENDER");
//                    gameState = GameState.WAITFORPLAYERACTION;
//                } else {
//                    System.out.println("BUST");
//                    revealHiddenCard();
//                    break;
//                }
//            }
//            else if (gameState == GameState.PLAYERSTAND) {
//                break;
//            }
//            else if (gameState == GameState.PLAYERSURRENDER) {
//                break;
//            }
//            else if (gameState == GameState.PLAYERDOUBLE) {
//                break;
//            }
//        }
//    }

    public boolean checkBlackJack() {
        if (playerScore == 21 && dealerScore != 21) {
//            revealHiddenCard();
//            getDealer().playerStands();
            gameState = GameState.PLAYERBLACKJACK;
            return true;
        }
        return false;
    }

//    private boolean checkDouble() {
//        if (gameState == GameState.PLAYERDOUBLE) {
//            System.out.println("♣ ♦ ♥ ♠ DOUBLE ♠ ♥ ♦ ♣");
//            if ((dealerScore >= playerScore && dealerScore < 22) || playerScore > 21) {
//                return false;
//            } else {
//                Utils.printWin();
//                System.out.println("\n♣ ♦ ♥ ♠ You have WON by DOUBLING, you've acquired' " + bet.getAmount() * 2 + " chips ♠ ♥ ♦ ♣");
//            }
//            return true;
//        }
//        return false;
//    }

    public void checkWinOrLose() {
        if ((dealerScore >= playerScore && dealerScore < 22) || playerScore > 21) {
            Utils.printLose();
            System.out.println("\n♣ ♦ ♥ ♠ You have LOST, say goodbye to your " + bet.getAmount() + " chips ♠ ♥ ♦ ♣");
            gameState = GameState.PLAYERLOSE;
        } else {
            Utils.printWin();
            System.out.println("\n♣ ♦ ♥ ♠ You have WON, you've acquired' " + bet.getAmount() * 2 + " chips ♠ ♥ ♦ ♣");
            gameState = GameState.PLAYERWIN;
        }
        System.out.println("♣ ♦ ♥ ♠ Dealer score: " + dealerScore + " Player score: " + playerScore + " ♠ ♥ ♦ ♣");
    }

    public void revealHiddenCard() {
        System.out.println("\nDealer reveals hidden card");
        System.out.println("Dealers cards: " + this.dealer.getHand().getCards());
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
