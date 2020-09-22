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
    public void startGame(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        this.blackJackService.startGame(profile.getUsername());
    }

    @PostMapping("/hit")
    public void playerHit() {
        this.blackJackService.playerHit();
    }

    @PostMapping("/stand")
    public void playerStand() {
        this.blackJackService.playerStand();
    }

    @PostMapping("/surrender")
    public void playerSurrender() {
        this.blackJackService.playerSurrender();
    }

    @PostMapping("/double")
    public void playerDouble() {
        this.blackJackService.playerDouble();
    }

    @PostMapping("/placebet")
    public void playerPlaceBet(Authentication authentication, @RequestBody Bet bet) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();

        this.blackJackService.placeBet(profile.getUsername(), bet.betAmount);
    }
}
