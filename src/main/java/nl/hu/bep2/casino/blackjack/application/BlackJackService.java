package nl.hu.bep2.casino.blackjack.application;

import nl.hu.bep2.casino.blackjack.data.BlackJack;
import nl.hu.bep2.casino.blackjack.data.BlackJackRepository;
import nl.hu.bep2.casino.blackjack.domain.Bet;
import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;
import nl.hu.bep2.casino.blackjack.domain.enums.GameState;
import nl.hu.bep2.casino.chips.application.ChipsService;
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
    private final BlackJackRepository blackJackRepository;
    private final BlackjackGame blackjackGame;
    private final ChipsService chipsService;

    private User user;
    private Chips chips;
    private BlackJack blackJack;

    public BlackJackService(SpringUserRepository userRepository, SpringChipsRepository chipsRepository, BlackJackRepository blackJackRepository, BlackjackGame blackjackGame, ChipsService chipsService) {
        this.userRepository = userRepository;
        this.chipsRepository = chipsRepository;
        this.blackJackRepository = blackJackRepository;
        this.blackjackGame = blackjackGame;
        this.chipsService = chipsService;
    }

    public void startGame(String username, Long bet) {
        //create new blackjack in repository
        blackJack = new BlackJack((long) this.blackjackGame.getPlayerScore(),(long) this.blackjackGame.getDealerScore(), this.blackjackGame.getGameState(), bet,  username);
        this.blackJackRepository.save(blackJack);

        chipsService.withdrawChips(username, bet);
        blackjackGame.setBet(new Bet(bet));
        System.out.println(username + " placed a bet of " + bet + " chips");

        user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        chips = this.chipsRepository.findByUser(user)
                .orElse(new Chips(user, 0L));

        this.blackjackGame.initializeGame(username);

        if (this.blackjackGame.getGameState() == GameState.PLAYERBLACKJACK) {
            playerStand(username);
        }
    }

    public void playerHit(String username) {
        if (this.blackjackGame.getPlayerScore() < 21) {
            this.blackjackGame.getDealer().drawCardForPlayer();
            this.blackjackGame.updateCardsScores();

            if (this.blackjackGame.getPlayerScore() < 22) {

                System.out.println("Your cards: " + this.blackjackGame.getPlayer().getHand().getCards());

                this.blackjackGame.updateCardsScores();

                this.blackjackGame.setGameState(GameState.PLAYERHIT);
            } else {
                this.blackjackGame.setGameState(GameState.PLAYERLOSE);
                playerStand(username);
            }
            blackJack.update((long) this.blackjackGame.getPlayerScore(),(long) this.blackjackGame.getDealerScore(), this.blackjackGame.getGameState(), false);
            this.blackJackRepository.save(blackJack);
        } else {
            System.out.println("Hit not possible");
        }
    }

    public void playerStand(String username) {
        this.blackjackGame.revealHiddenCard();
        this.blackjackGame.getDealer().playerStands();
        this.blackjackGame.setGameState(GameState.PLAYERSTAND);

        System.out.println("Dealer has drawn card(s)");
        System.out.println("Dealers cards: " + this.blackjackGame.getDealer().getHand().getCards());

        this.blackjackGame.updateCardsScores();

        if (this.blackjackGame.getDealerScore() >= this.blackjackGame.getPlayerScore() &&
                this.blackjackGame.getDealerScore() < 22 || this.blackjackGame.getPlayerScore() > 21) {
            this.blackjackGame.setGameState(GameState.PLAYERLOSE);
        } else {
            if (this.blackjackGame.getGameState() != GameState.PLAYERDOUBLE &&
                    this.blackjackGame.getGameState() != GameState.PLAYERBLACKJACK) {
                this.blackjackGame.setGameState(GameState.PLAYERWIN);
            }
        }
        blackJack.update((long) this.blackjackGame.getPlayerScore(),(long) this.blackjackGame.getDealerScore(), this.blackjackGame.getGameState(), true);
        this.blackJackRepository.save(blackJack);

        this.chipsService.payOut(username,
                this.blackjackGame.getGameState(),
                this.blackjackGame.getBet().getAmount());
        System.out.println("♣ ♦ ♥ ♠ Dealer score: " + this.blackjackGame.getDealerScore() + " Player score: " + this.blackjackGame.getPlayerScore() + " ♠ ♥ ♦ ♣");
    }

    public void playerSurrender(String username) {
        this.blackjackGame.setGameState(GameState.PLAYERSURRENDER);
        this.chipsService.payOut(username,
                this.blackjackGame.getGameState(),
                this.blackjackGame.getBet().getAmount());
        System.out.println("♣ ♦ ♥ ♠ Dealer score: " + this.blackjackGame.getDealerScore() + " Player score: " + this.blackjackGame.getPlayerScore() + " ♠ ♥ ♦ ♣");
        blackJack.update((long) this.blackjackGame.getPlayerScore(),(long) this.blackjackGame.getDealerScore(), this.blackjackGame.getGameState(), true);
        this.blackJackRepository.save(blackJack);
    }

    public void playerDouble(String username) {
        chips.remove(this.blackjackGame.getBet().getAmount());
        this.chipsRepository.save(chips);

        chips = this.chipsRepository.findByUser(user)
                .orElse(new Chips(user, 0L));

        this.blackjackGame.getBet().setAmount(this.blackjackGame.getBet().getAmount() * 2);

        this.blackjackGame.getDealer().drawCardForPlayer();
        System.out.println("Your cards: " + this.blackjackGame.getPlayer().getHand().getCards());
        this.blackjackGame.revealHiddenCard();
        this.blackjackGame.getDealer().playerStands();
        System.out.println("Dealer has drawn card(s)");
        System.out.println("Dealers cards: " + this.blackjackGame.getDealer().getHand().getCards());

        this.blackjackGame.updateCardsScores();

        this.blackjackGame.setGameState(GameState.PLAYERDOUBLE);
        blackJack.update((long) this.blackjackGame.getPlayerScore(),(long) this.blackjackGame.getDealerScore(), this.blackjackGame.getGameState(), false);
        this.blackJackRepository.save(blackJack);
        playerStand(username);
    }
}
