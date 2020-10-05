package nl.hu.bep2.casino.blackjack.application;

import nl.hu.bep2.casino.blackjack.data.Blackjack;
import nl.hu.bep2.casino.blackjack.data.BlackjackRepository;
import nl.hu.bep2.casino.blackjack.domain.Bet;
import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;
import nl.hu.bep2.casino.blackjack.domain.enums.GameState;
import nl.hu.bep2.casino.blackjack.presentation.dto.BlackjackDTO;
import nl.hu.bep2.casino.chips.application.ChipsService;
import nl.hu.bep2.casino.chips.data.Chips;
import nl.hu.bep2.casino.chips.data.SpringChipsRepository;
import nl.hu.bep2.casino.security.data.SpringUserRepository;
import nl.hu.bep2.casino.security.data.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public class BlackjackService {
    private final SpringUserRepository userRepository;
    private final BlackjackRepository blackJackRepository;
    private final ChipsService chipsService;
    private final BlackjackGameFactory blackJackGameFactory;

    public BlackjackService(SpringUserRepository userRepository, BlackjackRepository blackJackRepository, ChipsService chipsService, BlackjackGameFactory blackJackGameFactory) {
        this.userRepository = userRepository;
        this.blackJackRepository = blackJackRepository;
        this.chipsService = chipsService;
        this.blackJackGameFactory = blackJackGameFactory;
    }

    public BlackjackDTO startGame(String username, Long bet) {
        var user = retrieveUser(username);

        //Check if a game for this user already exists
        Optional<Blackjack> lastBlackJack = this.blackJackRepository.findTopByUserAndGameDoneOrderByIdDesc(user, false);
        if (lastBlackJack.isPresent()) {
            throw new RuntimeException("Game already exists");
        }

        //Make new blackjackgame
        var blackjackGame = blackJackGameFactory.create();

        //Withdraws chips and places bet
        this.chipsService.withdrawChips(username, bet);
        blackjackGame.setBet(new Bet(bet));
        System.out.println(username + " placed a bet of " + bet + " chips");


        blackjackGame.initializeGame(username);

        this.blackJackRepository.save(new Blackjack(blackjackGame, false, user));

        if (blackjackGame.getGameState() == GameState.PLAYERBLACKJACK) {
            return playerStand(username);
        }

        return new BlackjackDTO(blackjackGame.getPlayer().getHand(),
                blackjackGame.getDealer().getHand(),
                blackjackGame.getGameState());
    }

    public BlackjackDTO playerHit(String username) {
        var user = retrieveUser(username);
        var blackJack = retrieveBlackJackGame(user);
        var blackjackGame = blackJack.getBlackjackGame();

        if (blackjackGame.getPlayerScore() < 22) {
            blackjackGame.getDealer().drawCardForPlayer();
            blackjackGame.updateCardsScores();

            if (blackjackGame.getPlayerScore() < 22) {

                System.out.println("Your cards: " + blackjackGame.getPlayer().getHand().getCards());

                blackjackGame.updateCardsScores();

                blackjackGame.setGameState(GameState.PLAYERHIT);
            } else {
                blackjackGame.setGameState(GameState.PLAYERLOSE);
                playerStand(username);
                return null;
            }

            this.blackJackRepository.save(blackJack);
        } else {
            System.out.println("Hit not possible");
        }
        return new BlackjackDTO(blackjackGame.getPlayer().getHand(),
                blackjackGame.getDealer().getHand(),
                blackjackGame.getGameState());
    }

    public BlackjackDTO playerStand(String username) {
        var user = retrieveUser(username);
        var blackJack = retrieveBlackJackGame(user);
        var blackjackGame = blackJack.getBlackjackGame();

        blackjackGame.revealHiddenCard();
        blackjackGame.getDealer().playerStands();
        blackjackGame.setGameState(GameState.PLAYERSTAND);

        System.out.println("Dealer has drawn card(s)");
        System.out.println("Dealers cards: " + blackjackGame.getDealer().getHand().getCards());

        blackjackGame.updateCardsScores();

        if (blackjackGame.getDealerScore() >= blackjackGame.getPlayerScore() && blackjackGame.getDealerScore() < 22 || blackjackGame.getPlayerScore() > 21) {
            blackjackGame.setGameState(GameState.PLAYERLOSE);
        }
        else {
            if (blackjackGame.getGameState() != GameState.PLAYERDOUBLE && blackjackGame.getGameState() != GameState.PLAYERBLACKJACK) {
                blackjackGame.setGameState(GameState.PLAYERWIN);
            }
        }

        this.chipsService.payOut(username, blackjackGame.getGameState(), blackjackGame.getBet().getAmount());

        blackJack.setGameDone(true);
        this.blackJackRepository.save(blackJack);

        System.out.println("♣ ♦ ♥ ♠ Dealer score: " + blackjackGame.getDealerScore() + " Player score: " + blackjackGame.getPlayerScore() + " Game state: " +blackjackGame.getGameState() + " ♠ ♥ ♦ ♣");

        return new BlackjackDTO(blackjackGame.getPlayer().getHand(),
                blackjackGame.getDealer().getHand(),
                blackjackGame.getGameState());
    }

    public BlackjackDTO playerSurrender(String username) {
        var user = retrieveUser(username);
        var blackJack = retrieveBlackJackGame(user);
        var blackjackGame = blackJack.getBlackjackGame();

        blackjackGame.setGameState(GameState.PLAYERSURRENDER);
        this.chipsService.payOut(username,
                blackjackGame.getGameState(),
                blackjackGame.getBet().getAmount());
        System.out.println("♣ ♦ ♥ ♠ Dealer score: " + blackjackGame.getDealerScore() + " Player score: " + blackjackGame.getPlayerScore() + " ♠ ♥ ♦ ♣");

        blackJack.setGameDone(true);
        this.blackJackRepository.save(blackJack);

        return new BlackjackDTO(blackjackGame.getPlayer().getHand(),
                blackjackGame.getDealer().getHand(),
                blackjackGame.getGameState());
    }

    public BlackjackDTO playerDouble(String username) {
        var user = retrieveUser(username);
        var blackJack = retrieveBlackJackGame(user);
        var blackjackGame = blackJack.getBlackjackGame();

        this.chipsService.withdrawChips(username, blackjackGame.getBet().getAmount());

        blackjackGame.getBet().setAmount(blackjackGame.getBet().getAmount() * 2);

        blackjackGame.getDealer().drawCardForPlayer();
        System.out.println("Your cards: " + blackjackGame.getPlayer().getHand().getCards());
        blackjackGame.revealHiddenCard();
        blackjackGame.getDealer().playerStands();
        System.out.println("Dealer has drawn card(s)");
        System.out.println("Dealers cards: " + blackjackGame.getDealer().getHand().getCards());

        blackjackGame.updateCardsScores();

        blackjackGame.setGameState(GameState.PLAYERDOUBLE);
        this.blackJackRepository.save(blackJack);

        return playerStand(username);
    }

    private Blackjack retrieveBlackJackGame(User user) {
        return this.blackJackRepository.findTopByUserAndGameDoneOrderByIdDesc(user, false)
                .orElseThrow(RuntimeException::new);
    }

    private User retrieveUser(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
