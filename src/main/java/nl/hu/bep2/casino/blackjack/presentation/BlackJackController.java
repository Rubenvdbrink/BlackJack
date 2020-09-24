package nl.hu.bep2.casino.blackjack.presentation;

import nl.hu.bep2.casino.blackjack.application.BlackJackService;
import nl.hu.bep2.casino.blackjack.presentation.dto.Bet;
import nl.hu.bep2.casino.security.data.UserProfile;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blackjack")
public class BlackJackController {
    private final BlackJackService blackJackService;

    public BlackJackController(BlackJackService blackJackService) { this.blackJackService = blackJackService; }

    @PostMapping("/startgame")
    public void startGame(Authentication authentication, @RequestBody Bet bet) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        this.blackJackService.startGame(profile.getUsername(), bet.betAmount);
    }

    @PostMapping("/hit")
    public void playerHit(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        this.blackJackService.playerHit(profile.getUsername());
    }

    @PostMapping("/stand")
    public void playerStand(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        this.blackJackService.playerStand(profile.getUsername());
    }

    @PostMapping("/surrender")
    public void playerSurrender(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        this.blackJackService.playerSurrender(profile.getUsername());
    }

    @PostMapping("/double")
    public void playerDouble(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        this.blackJackService.playerDouble(profile.getUsername());
    }
}
