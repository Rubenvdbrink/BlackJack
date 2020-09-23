package nl.hu.bep2.casino.blackjack.application;

import nl.hu.bep2.casino.blackjack.domain.Bet;
import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;
import nl.hu.bep2.casino.blackjack.domain.enums.GameState;
import nl.hu.bep2.casino.chips.data.Chips;
import nl.hu.bep2.casino.chips.data.SpringChipsRepository;
import nl.hu.bep2.casino.security.data.SpringUserRepository;
import nl.hu.bep2.casino.security.data.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class BlackJackService {
    private final SpringUserRepository userRepository;
    private final SpringChipsRepository chipsRepository;
    private final BlackjackGame blackjackGame;

    private User user;
    private Chips chips;

    public BlackJackService(SpringUserRepository userRepository, SpringChipsRepository chipsRepository, BlackjackGame blackjackGame) {
        this.userRepository = userRepository;
        this.chipsRepository = chipsRepository;
        this.blackjackGame = blackjackGame;
    }

    public void startGame(String username) throws InterruptedException {

        user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        chips = this.chipsRepository.findByUser(user)
                .orElse(new Chips(user, 0L));

        this.blackjackGame.getPlayer().setUsername(username);
        System.out.println("Welcome, " + this.blackjackGame.getPlayer().getUsername() + " to blackjack!");

        System.out.println("♣ ♦ ♥ ♠ Please place your bets now! ♠ ♥ ♦ ♣");
        while (blackjackGame.getBet() == null){
            Thread.sleep(1000);
        }

        System.out.println("♣ ♦ ♥ ♠ " + username + " has placed a bet of " + blackjackGame.getBet().getAmount() + " chips ♠ ♥ ♦ ♣");
        System.out.println("♣ ♦ ♥ ♠ Deck is getting shuffled ♠ ♥ ♦ ♣");
        this.blackjackGame.getDealer().shuffleDeck();
        this.blackjackGame.setGameState(GameState.STARTOFGAME);
        this.blackjackGame.startingRound();

        //simulate blackjack for player
//        this.blackjackGame.fakeBlackJackForPlayer();
        
        if (this.blackjackGame.checkBlackJack()) {
            this.blackjackGame.revealHiddenCard();
            chips.deposit(blackjackGame.getBet().getAmount() * 5);
            this.chipsRepository.save(chips);
            return;
        }

        System.out.println("What is your next move? HIT, STAND, DOUBLE OR SURRENDER");
        while (true) {
            if (this.blackjackGame.getGameState() == GameState.WAITFORPLAYERACTION) {
                Thread.sleep(1000);
            }
            else if (this.blackjackGame.getGameState() == GameState.PLAYERHIT) {
                if (this.blackjackGame.getPlayerScore() < 22) {
                    System.out.println("What is your next move? HIT, STAND, DOUBLE OR SURRENDER");
                    this.blackjackGame.setGameState(GameState.WAITFORPLAYERACTION);
                } else {
                    System.out.println("BUST");
                    break;
                }
            }
            else if (this.blackjackGame.getGameState() == GameState.PLAYERSTAND) {
                break;
            }
        }
//        TODO need to find out how enum switch case works
//        while (true) {
//            GameState gs = this.blackjackGame.getGameState();
//            switch (gs)    {
//                case GameState.WAITFORPLAYERACTION:
//                    Thread.sleep(1000);
//
//                case GameState.PLAYERHIT:
//                    if (this.blackjackGame.getPlayerScore() < 22) {
//                        System.out.println("What is your next move? HIT, STAND, DOUBLE OR SURRENDER");
//                        this.blackjackGame.setGameState(GameState.WAITFORPLAYERACTION);
//                    } else {
//                        System.out.println("BUST");
//                        break;
//            }
//        }

        this.blackjackGame.checkWinOrLose();
        System.out.println("♣ ♦ ♥ ♠ Dealer score: " + this.blackjackGame.getDealerScore() + " Player score: " + this.blackjackGame.getPlayerScore() + " ♠ ♥ ♦ ♣");
    }

    public void placeBet(String username, Long betAmount) {
        chips.remove(betAmount);
        this.chipsRepository.save(chips);

        //make new bet and put it in blackJackData
        blackjackGame.setBet(new Bet(betAmount));
        System.out.println(username + " placed a bet of " + betAmount + " chips");
    }

    //TODO
    public void playerHit() {
        System.out.println("HIT (WIP)");

        this.blackjackGame.getDealer().drawCardForPlayer();

        System.out.println("Your cards: " + this.blackjackGame.getPlayer().getHand().getCards());

        this.blackjackGame.updateCardsScores();

        this.blackjackGame.setGameState(GameState.PLAYERHIT);
    }

    //TODO
    public void playerStand() {
        System.out.println("STAND (WIP)");

        this.blackjackGame.revealHiddenCard();
        this.blackjackGame.getDealer().playerStands();

        System.out.println("Dealer has drawn card(s)");
        System.out.println("Dealers cards: " + this.blackjackGame.getDealer().getHand().getCards());

        this.blackjackGame.updateCardsScores();

        this.blackjackGame.setGameState(GameState.PLAYERSTAND);
    }

    //TODO
    public void playerSurrender() {
        System.out.println("SURRENDER (WIP)");
    }

    //TODO
    public void playerDouble() {
        System.out.println("DOUBLE (WIP)");
    }
}
