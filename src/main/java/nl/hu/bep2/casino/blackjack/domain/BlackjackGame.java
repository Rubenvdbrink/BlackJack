package nl.hu.bep2.casino.blackjack.domain;


import nl.hu.bep2.casino.blackjack.domain.enums.GameState;
import nl.hu.bep2.casino.blackjack.domain.enums.Rank;
import nl.hu.bep2.casino.blackjack.domain.enums.Suit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BlackjackGame extends Game implements Serializable {
    private Player player;
    private Dealer dealer;
    private int playerScore = 0;
    private int dealerScore = 0;
    private Bet bet;
    private GameState gameState = GameState.STARTOFGAME;

    public BlackjackGame(Player player, Dealer dealer) {
        this.player = player;
        this.dealer = dealer;
    }

    @Override
    public boolean initializeGame(String username, Long bet) {
        Utils.printWelcome();
        this.bet = new Bet(bet);

        System.out.println("Welcome, to blackjack!");
        System.out.println("♣ ♦ ♥ ♠ " + username + " has placed a bet of " + this.bet.getAmount() + " chips ♠ ♥ ♦ ♣");

        System.out.println("♣ ♦ ♥ ♠ Deck is getting shuffled ♠ ♥ ♦ ♣");
        this.dealer.shuffleDeck();

        startingRound();

//        fakeBlackJackForPlayer();
        return checkBlackJack();
    }

    //ToDo replace code from service to here
    public void playerHit() {
        if (this.playerScore < 22) {
            this.dealer.drawCardForPlayer();
            updateCardsScores();

            if (this.playerScore < 22) {

                System.out.println("Your cards: " + this.player.getHand().getCards());

                updateCardsScores();

                this.gameState = GameState.PLAYERHIT;
            } else {
                this.gameState = GameState.PLAYERLOSE;
            }
        }
    }

    public void playerStand() {
        revealHiddenCard();
        this.dealer.playerStands();

        System.out.println("Dealer has drawn card(s)");
        System.out.println("Dealers cards: " + this.dealer.getHand().getCards());

        updateCardsScores();
        if (this.gameState == GameState.PLAYERSURRENDER) {
            return;
        }
//        this.gameState = GameState.PLAYERSTAND;
        if (this.dealerScore
                >= this.playerScore
                && this.dealerScore < 22
                || this.playerScore > 21) {
           this.gameState = GameState.PLAYERLOSE;
        } else {
            if (this.gameState
                    != GameState.PLAYERDOUBLE
                    && this.gameState
                    != GameState.PLAYERBLACKJACK) {
                this.gameState = GameState.PLAYERWIN;
            }
        }
        System.out.println("♣ ♦ ♥ ♠ Dealer score: " + this.dealerScore + " Player score: " + this.playerScore + " Game state: " + this.gameState + " ♠ ♥ ♦ ♣");
    }

    public void playerSurrender() {
        this.gameState = GameState.PLAYERSURRENDER;
    }

    public boolean playerDouble() {
        if(this.gameState == GameState.STARTOFGAME) {

            dealer.drawCardForPlayer();
            System.out.println("Your cards: " + player.getHand().getCards());

            revealHiddenCard();

            dealer.playerStands();
            System.out.println("Dealer has drawn card(s)");
            System.out.println("Dealers cards: " + dealer.getHand().getCards());

            updateCardsScores();

            this.gameState = GameState.PLAYERDOUBLE;

            return true;
        }
        return false;
    }

    public void startingRound() {
        System.out.println("\n♣ ♦ ♥ ♠ Handing out cards ♠ ♥ ♦ ♣");
        this.dealer.startGameHandOutCards();
        System.out.println("\nYour cards: " + this.player.getHand().getCards());
        System.out.println("Dealers cards: " + this.dealer.getHand().getCards().get(0) + " and one non visible card");
        updateCardsScores();
    }

    public void revealHiddenCard() {
        System.out.println("\nDealer reveals hidden card");
        System.out.println("Dealers cards: " + this.dealer.getHand().getCards());
    }

    public boolean checkBlackJack() {
        if (playerScore == 21 && dealerScore != 21) {
            gameState = GameState.PLAYERBLACKJACK;
            return true;
        }
        return false;
    }


    public void updateCardsScores() {
        playerScore = player.totalScoreOfCards();
        dealerScore = dealer.totalScoreOfCards();
    }

    //TODO remove before version 1.0, this is just for testing purposes
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

    public Dealer getDealer() {
        return dealer;
    }

    public Bet getBet() {
        return bet;
    }

    //ToDo remove before version 1.0
    public void setBet(Bet bet) { this.bet = bet; }
    public int getPlayerScore() { return playerScore; }
    public int getDealerScore() { return dealerScore; }
    //

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
