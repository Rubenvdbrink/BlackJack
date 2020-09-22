package nl.hu.bep2.casino.blackjack.application;

import nl.hu.bep2.casino.blackjack.domain.Bet;
import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;
import nl.hu.bep2.casino.blackjack.domain.Card;
import nl.hu.bep2.casino.blackjack.domain.enums.Rank;
import nl.hu.bep2.casino.blackjack.domain.enums.Suit;
import nl.hu.bep2.casino.chips.data.Chips;
import nl.hu.bep2.casino.chips.data.SpringChipsRepository;
import nl.hu.bep2.casino.security.data.SpringUserRepository;
import nl.hu.bep2.casino.security.data.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class BlackJackService {
    private final SpringUserRepository userRepository;
    private final SpringChipsRepository chipsRepository;
    private final BlackjackGame blackjackGame;

    public BlackJackService(SpringUserRepository userRepository, SpringChipsRepository chipsRepository, BlackjackGame blackjackGame) {
        this.userRepository = userRepository;
        this.chipsRepository = chipsRepository;
        this.blackjackGame = blackjackGame;
    }

    public void startGame(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Chips chips = this.chipsRepository.findByUser(user)
                .orElse(new Chips(user, 0L));

        this.blackjackGame.getPlayer().setUsername(username);
        System.out.println("Welcome, " + this.blackjackGame.getPlayer().getUsername() + " to blackjack!");

        System.out.println("♣ ♦ ♥ ♠ Please place your bets now! ♠ ♥ ♦ ♣");
        while (blackjackGame.getBet() == (null)){
            // do nothing
        }
        System.out.println("♣ ♦ ♥ ♠ " + username + " has placed a bet of " + blackjackGame.getBet().getAmount() + " chips ♠ ♥ ♦ ♣");
        this.blackjackGame.getDealer().shuffleDeck();
        this.blackjackGame.startingRound();

        //simulate blackjack for player
//        List<Card> list = new ArrayList<>();
//        list.add(new Card(Rank.ACE, Suit.DIAMONDS));
//        list.add(new Card(Rank.TEN, Suit.DIAMONDS));
//        this.blackjackGame.getPlayer().getHand().setCards(list);
//
//        System.out.println(this.blackjackGame.getPlayer().getHand().getCards());
//
//        this.blackjackGame.updateCardsScores();
        //check blackjack
        if (this.blackjackGame.checkBlackJack()) {
            chips.deposit(blackjackGame.getBet().getAmount() * 5);
            this.chipsRepository.save(chips);
        }
    }

    public void placeBet(String username, Long betAmount) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Chips chips = this.chipsRepository.findByUser(user)
                .orElse(new Chips(user, 0L));

        //remove chips amount from the users chips repo and save the repo
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
    }

    //TODO
    public void playerStand() {
        System.out.println("STAND (WIP)");
        this.blackjackGame.getDealer().playerStands();
        System.out.println("Dealers cards: " + this.blackjackGame.getDealer().getHand().getCards());
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
