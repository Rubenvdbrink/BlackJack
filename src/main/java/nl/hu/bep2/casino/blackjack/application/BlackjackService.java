package nl.hu.bep2.casino.blackjack.application;

import nl.hu.bep2.casino.blackjack.data.Blackjack;
import nl.hu.bep2.casino.blackjack.data.BlackjackRepository;
import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;
import nl.hu.bep2.casino.blackjack.domain.enums.GameState;
import nl.hu.bep2.casino.blackjack.domain.strategies.*;
import nl.hu.bep2.casino.chips.application.ChipsService;
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

    public BlackjackGame startOrContinueGame(String username, Long bet) {
        var user = retrieveUser(username);

        //Check if a game for this user already exists
        Optional<Blackjack> lastBlackJack = this.blackJackRepository.findTopByUserAndGameDoneOrderByIdDesc(user, false);
        if (lastBlackJack.isPresent()) {
            //Returns the BlackjackGame object of the last blackjack game that the player started
            return lastBlackJack.get().getBlackjackGame();
        }

        //Make new blackjackgame and place bet
        var blackjackGame = blackJackGameFactory.create(bet);

        //Withdraws chips
        this.chipsService.withdrawChips(username, bet);

        //Starts game
        if (new InitializeGameStrategy().doAction(blackjackGame)) {
            this.blackJackRepository.save(new Blackjack(blackjackGame, false, user));
            return playerStandOrDealersTurn(username);
        }

        //Saves new blackjackgame in database
        this.blackJackRepository.save(new Blackjack(blackjackGame, false, user));

        return blackjackGame;
    }

    public BlackjackGame playerHit(String username) {
        var blackjackGame = retrieveBlackjack(username).getBlackjackGame();

//        if (new HitStrategy().doAction(blackjackGame)) {
//            if (blackjackGame.getGameState() == GameState.PLAYERLOSE) {
//                return playerStandOrDealersTurn(username);
//            }
//        } else {
//            throw new RuntimeException("♣ ♦ ♥ ♠ Can't hit at this moment! ♠ ♥ ♦ ♣");
//        }

        if (!new HitStrategy().doAction(blackjackGame)) {
            return playerStandOrDealersTurn(username);
        }

        this.blackJackRepository.save(retrieveBlackjack(username));

        return blackjackGame;
    }

    public BlackjackGame playerStandOrDealersTurn(String username) {
        var blackJack = retrieveBlackjack(username);
        var blackjackGame = retrieveBlackjack(username).getBlackjackGame();

        new StandOrDealersTurnStrategy().doAction(blackjackGame);

        this.chipsService.payOut(username, blackjackGame.getGameState(), blackjackGame.getBet().getAmount());

        blackJack.setGameDone(true);
        this.blackJackRepository.save(blackJack);

        return blackjackGame;
    }

    public BlackjackGame playerSurrender(String username) {
        var blackjackGame = retrieveBlackjack(username).getBlackjackGame();

        new SurrenderStrategy().doAction(blackjackGame);
        return playerStandOrDealersTurn(username);
    }

    public BlackjackGame playerDouble(String username) {
        var blackjackGame = retrieveBlackjack(username).getBlackjackGame();

        this.chipsService.withdrawChips(username, blackjackGame.getBet().getAmount());

        if (new DoubleStrategy().doAction(blackjackGame)) {
            this.blackJackRepository.save(retrieveBlackjack(username));
            return playerStandOrDealersTurn(username);
        }
        throw new RuntimeException("♣ ♦ ♥ ♠ Can't double at this moment!, You can only double when it's your first move ♠ ♥ ♦ ♣");
    }

    //gets the user object by username
    private User retrieveUser(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("♣ ♦ ♥ ♠ User with username: " + username + " does not exist! ♠ ♥ ♦ ♣"));
    }

    //gets the last Blackjack data object by username
    private Blackjack retrieveBlackjack(String username) {
        return this.blackJackRepository.findTopByUserAndGameDoneOrderByIdDesc(retrieveUser(username), false)
                .orElseThrow(() -> new RuntimeException("♣ ♦ ♥ ♠ Game has not started yet! ♠ ♥ ♦ ♣"));
    }
}
