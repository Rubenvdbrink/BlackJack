package nl.hu.bep2.casino.blackjack.presentation;

import nl.hu.bep2.casino.blackjack.application.BlackjackService;
import nl.hu.bep2.casino.blackjack.presentation.dto.BetDTO;
import nl.hu.bep2.casino.blackjack.presentation.dto.BlackjackDTO;
import nl.hu.bep2.casino.security.data.UserProfile;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blackjack")
public class BlackJackController {
    private final BlackjackService blackJackService;

    public BlackJackController(BlackjackService blackJackService) { this.blackJackService = blackJackService; }

    @PostMapping("/startgame")
    public BlackjackDTO startGame(Authentication authentication, @RequestBody BetDTO bet) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        return blackJackService.startOrContinueGame(profile.getUsername(), bet.betAmount);
    }

    @PostMapping("/hit")
    public BlackjackDTO playerHit(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        return blackJackService.playerHit(profile.getUsername());
    }

    @PostMapping("/stand")
    public BlackjackDTO playerStand(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        return blackJackService.playerStandOrDealersTurn(profile.getUsername());
    }

    @PostMapping("/surrender")
    public BlackjackDTO playerSurrender(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        return blackJackService.playerSurrender(profile.getUsername());
    }

    @PostMapping("/double")
    public BlackjackDTO playerDouble(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        return blackJackService.playerDouble(profile.getUsername());
    }
}
