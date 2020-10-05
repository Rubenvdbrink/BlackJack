package nl.hu.bep2.casino.blackjack.application;

import nl.hu.bep2.casino.blackjack.data.Blackjack;
import nl.hu.bep2.casino.blackjack.data.BlackjackRepository;
import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;
import nl.hu.bep2.casino.blackjack.domain.enums.GameState;
import nl.hu.bep2.casino.blackjack.presentation.dto.BlackjackDTO;
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
            //Returns the dto of the last blackjack game that the player started
            return lastBlackJack.get().getBlackjackGame();
        }

        //Make new blackjackgame
        var blackjackGame = blackJackGameFactory.create();

        //Withdraws chips
        this.chipsService.withdrawChips(username, bet);

        //Starts game and places bet
        if (blackjackGame.initializeGame(username, bet)) {
            this.blackJackRepository.save(new Blackjack(blackjackGame, false, user));
            return playerStandOrDealersTurn(username);
        }

        //Saves new blackjackgame in database
        this.blackJackRepository.save(new Blackjack(blackjackGame, false, user));

        return blackjackGame;
    }

    public BlackjackGame playerHit(String username) {
        var user = retrieveUser(username);
        var blackJack = retrieveBlackJackGame(user);
        var blackjackGame = blackJack.getBlackjackGame();

        if (blackjackGame.playerHit()) {
            if (blackjackGame.getGameState() == GameState.PLAYERLOSE) {
                return playerStandOrDealersTurn(username);
            }
        } else {
            throw new RuntimeException("♣ ♦ ♥ ♠ Can't hit at this moment! ♠ ♥ ♦ ♣");
        }

        this.blackJackRepository.save(blackJack);

        return blackjackGame;
    }

    public BlackjackGame playerStandOrDealersTurn(String username) {
        var user = retrieveUser(username);
        var blackJack = retrieveBlackJackGame(user);
        var blackjackGame = blackJack.getBlackjackGame();

        blackjackGame.playerStandOrDealersTurn();

        this.chipsService.payOut(username, blackjackGame.getGameState(), blackjackGame.getBet().getAmount());

        blackJack.setGameDone(true);
        this.blackJackRepository.save(blackJack);

        return blackjackGame;
    }

    public BlackjackGame playerSurrender(String username) {
        var user = retrieveUser(username);
        var blackJack = retrieveBlackJackGame(user);
        var blackjackGame = blackJack.getBlackjackGame();

        blackjackGame.playerSurrender();
        return playerStandOrDealersTurn(username);
    }

    public BlackjackGame playerDouble(String username) {
        var user = retrieveUser(username);
        var blackJack = retrieveBlackJackGame(user);
        var blackjackGame = blackJack.getBlackjackGame();

        if (blackjackGame.playerDouble()) {
            this.chipsService.withdrawChips(username, blackjackGame.getBet().getAmount());
            blackjackGame.getBet().setAmount(blackjackGame.getBet().getAmount() * 2);
            this.blackJackRepository.save(blackJack);
            return playerStandOrDealersTurn(username);
        }
        throw new RuntimeException("♣ ♦ ♥ ♠ Can't double at this moment! ♠ ♥ ♦ ♣");
    }

    private Blackjack retrieveBlackJackGame(User user) {
        return this.blackJackRepository.findTopByUserAndGameDoneOrderByIdDesc(user, false)
                .orElseThrow(() -> new RuntimeException("♣ ♦ ♥ ♠ You did not start a game yet! ♠ ♥ ♦ ♣"));
    }

    private User retrieveUser(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException( "♣ ♦ ♥ ♠ User with username: " + username + " does not exist! ♠ ♥ ♦ ♣"));
    }
}
