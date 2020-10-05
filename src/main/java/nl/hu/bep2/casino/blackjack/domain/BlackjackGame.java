package nl.hu.bep2.casino.blackjack.domain;


import nl.hu.bep2.casino.blackjack.domain.enums.GameState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BlackjackGame implements Serializable {
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

    public boolean initializeGame(String username, Long bet) {
        Utils.printWelcome();
        this.bet = new Bet(bet);

        System.out.println("Welcome, to blackjack!");
        System.out.println("♣ ♦ ♥ ♠ " + username + " has placed a bet of " + this.bet.getAmount() + " chips ♠ ♥ ♦ ♣");

        System.out.println("♣ ♦ ♥ ♠ Deck is getting shuffled ♠ ♥ ♦ ♣");
        this.dealer.shuffleDeck();

        startingRound();

//        manipulateCards(new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.ACE, Suit.DIAMONDS), player);
//        manipulateCards(new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.ACE, Suit.DIAMONDS), dealer);

        checkDoubleAce(player);
        checkDoubleAce(dealer);
        return checkBlackJack();
    }

    private void startingRound() {
        System.out.println("\n♣ ♦ ♥ ♠ Handing out cards ♠ ♥ ♦ ♣");
        this.dealer.startGameHandOutCards();
        System.out.println("\nYour cards: " + this.player.getHand().getCards());
        System.out.println("Dealers cards: " + this.dealer.getHand().getCards().get(0) + " and one non visible card");
        updateCardsScores();
    }

    /**
     *  \/ ACTIONS \/
     */

    public boolean playerHit() {
        updateCardsScores();

        if (this.playerScore < 22) {
            this.dealer.drawCardForPlayer();
            updateCardsScores();

            System.out.println("Your cards: " + this.player.getHand().getCards());

            if (this.playerScore < 22) {
                this.gameState = GameState.PLAYERHIT;
            } else {
                if (!checkForAceAndChangeValue(player)) {
                    this.gameState = GameState.PLAYERLOSE;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void playerStandOrDealersTurn() {
        revealHiddenCard();
        this.dealer.playerStands();
        updateCardsScores();

        while (dealerScore > 21) {
            if (checkForAceAndChangeValue(this.dealer)) {
                this.dealer.playerStands();
            } else {
                break;
            }
        }

        revealHiddenCard();

        updateCardsScores();
        if (this.gameState == GameState.PLAYERSURRENDER) {
            return;
        }

        if (this.dealerScore
                > this.playerScore
                && this.dealerScore < 22
                || this.playerScore > 21) {
           this.gameState = GameState.PLAYERLOSE;
        } else {
            if (this.gameState
                    != GameState.PLAYERDOUBLE
                    && this.gameState
                    != GameState.PLAYERBLACKJACK
                    && this.gameState
                    != GameState.PLAYERPUSH) {
                this.gameState = GameState.PLAYERWIN;
            }
        }
        System.out.println("♣ ♦ ♥ ♠ Dealer score: " + this.dealerScore + " Player score: " + this.playerScore + " Game state: " + this.gameState + " ♠ ♥ ♦ ♣");
    }

    public void playerSurrender() {
        this.gameState = GameState.PLAYERSURRENDER;
    }

    public boolean playerDouble() {
        if (this.gameState == GameState.STARTOFGAME) {

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

    /**
     *  \/ CHECKS \/
     */

    private boolean checkBlackJack() {
        if (playerScore == 21 && dealerScore == 21) {
            gameState = GameState.PLAYERPUSH;
            return true;
        }
        else if (playerScore == 21) {
            gameState = GameState.PLAYERBLACKJACK;
            return true;
        }
        return false;
    }

    private boolean checkForAceAndChangeValue(Person person) {
        for (Card card : person.getHand().getCards()) {
            if (card.getValue() == 11) {
                card.setValue(1);
                return true;
            }
        }
        return false;
    }

    /**
     *  \/ OTHER \/
     */

    private void checkDoubleAce(Person person) {
        if (person.totalScoreOfCards() == 22) {
            checkForAceAndChangeValue(person);
        }
    }

    //only needed for console output
    private void revealHiddenCard() {
        System.out.println("\nDealer reveals hidden card");
        System.out.println("Dealers cards: " + this.dealer.getHand().getCards());
    }

    private void updateCardsScores() {
        playerScore = player.totalScoreOfCards();
        dealerScore = dealer.totalScoreOfCards();
    }

    //this is just for testing purposes
    private void manipulateCards(Card card1, Card card2, Person person) {
        List<Card> list = new ArrayList<>();
        list.add(card1);
        list.add(card2);
        person.getHand().setCards(list);

        updateCardsScores();

        System.out.println("manipulated cards! " + person.getHand().getCards());
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

    public GameState getGameState() {
        return gameState;
    }
}
