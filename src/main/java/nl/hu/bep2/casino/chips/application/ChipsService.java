package nl.hu.bep2.casino.chips.application;

import nl.hu.bep2.casino.blackjack.domain.enums.GameState;
import nl.hu.bep2.casino.chips.data.Chips;
import nl.hu.bep2.casino.chips.data.SpringChipsRepository;
import nl.hu.bep2.casino.security.data.User;
import nl.hu.bep2.casino.security.data.SpringUserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public class ChipsService {
    private final SpringUserRepository userRepository;
    private final SpringChipsRepository chipsRepository;

    public ChipsService(SpringUserRepository userRepository, SpringChipsRepository chipsRepository) {
        this.userRepository = userRepository;
        this.chipsRepository = chipsRepository;
    }

    public Optional<Chips> findBalance(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        System.out.println(username + " requested balance");
        return this.chipsRepository.findByUser(user);
    }

    public void depositChips(String username, Long amount) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Chips chips = this.chipsRepository.findByUser(user)
            .orElse(new Chips(user, 0L));
        chips.deposit(amount);

        System.out.println(amount + " chips have been deposited for " + username);
        this.chipsRepository.save(chips);
    }

    public void withdrawChips(String username, Long amount) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Chips chips = this.chipsRepository.findByUser(user)
                .orElse(new Chips(user, 0L));
        chips.remove(amount);

        System.out.println(amount + " chips have been withdrawed for " + username);
        this.chipsRepository.save(chips);
    }
    
    public void payOut(String username, GameState gameState, Long bet) {
        var user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        var chips = this.chipsRepository.findByUser(user)
                .orElse(new Chips(user, 0L));

        //PAYOUTS
        if (gameState == GameState.PLAYERBLACKJACK) {
            chips.deposit(bet * 5);
            System.out.println("WON BY BLACKJACK, you've won " + bet * 5 + " chips!, your new total is: " + chips.getAmount() + " chips!");
        }
        else if (gameState == GameState.PLAYERPUSH) {
            chips.deposit(bet);
            System.out.println("DRAW, your bet of " + bet + " chips has been returned, your new total is: " + chips.getAmount() + " chips!");
        }
        else if (gameState == GameState.PLAYERNORMALWIN) {
            chips.deposit(bet * 2);
            System.out.println("WIN, you've won " + bet * 2 + " chips!, your new total is: " + chips.getAmount() + " chips!");
        }
        else if (gameState == GameState.PLAYERDOUBLE) {
            chips.deposit(bet * 2);
            System.out.println("WON BY DOUBLING, you've won " + bet * 2 + " chips!, your new total is: " + chips.getAmount() + " chips!");
        }
        else if (gameState == GameState.PLAYERSURRENDER) {
            chips.deposit(bet / 2);
            System.out.println("SURRENDER, returned " + bet / 2 + " chips!, your new total is: " + chips.getAmount() + " chips!");
        }
        else if (gameState == GameState.PLAYERLOSE) {
            //deposit nothing because of lose
            System.out.println("LOSE, you've lost " + bet + " chips!, your new total is: " + chips.getAmount() + " chips!");
        }
        this.chipsRepository.save(chips);
    }
}
