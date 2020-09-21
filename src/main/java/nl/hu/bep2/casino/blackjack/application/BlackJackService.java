package nl.hu.bep2.casino.blackjack.application;

import nl.hu.bep2.casino.chips.data.Chips;
import nl.hu.bep2.casino.chips.data.SpringChipsRepository;
import nl.hu.bep2.casino.security.data.SpringUserRepository;
import nl.hu.bep2.casino.security.data.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BlackJackService {
    private final SpringUserRepository userRepository;
    private final SpringChipsRepository chipsRepository;

    public BlackJackService(SpringUserRepository userRepository, SpringChipsRepository chipsRepository) {
        this.userRepository = userRepository;
        this.chipsRepository = chipsRepository;
    }

    //TODO
    //A new bet needs to be made, now the bet only gets removed from the user's chips
    public void placeBet(String username, Long betAmount) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Chips chips = this.chipsRepository.findByUser(user)
                .orElse(new Chips(user, 0L));

        chips.remove(betAmount);

        this.chipsRepository.save(chips);
        System.out.println(username + " placed a bet of " + betAmount + " chips");
    }

    //TODO
    public void playerHit() {
        System.out.println("TEST");
    }
}
