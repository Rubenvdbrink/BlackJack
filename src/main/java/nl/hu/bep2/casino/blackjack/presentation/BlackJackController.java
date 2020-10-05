package nl.hu.bep2.casino.blackjack.presentation;

import nl.hu.bep2.casino.blackjack.application.BlackjackService;
import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;
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
        var blackjackGame = blackJackService.startOrContinueGame(profile.getUsername(), bet.betAmount);

        return new BlackjackDTO(
                blackjackGame.getGameState(),
                blackjackGame.getPlayer().getHand(),
                blackjackGame.getDealer().getHand());
    }

    @PostMapping("/hit")
    public BlackjackDTO playerHit(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        var blackjackGame = blackJackService.playerHit(profile.getUsername());

        return new BlackjackDTO(
                blackjackGame.getGameState(),
                blackjackGame.getPlayer().getHand(),
                blackjackGame.getDealer().getHand());
    }

    @PostMapping("/stand")
    public BlackjackDTO playerStand(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        var blackjackGame = blackJackService.playerStandOrDealersTurn(profile.getUsername());

        return new BlackjackDTO(
                blackjackGame.getGameState(),
                blackjackGame.getPlayer().getHand(),
                blackjackGame.getDealer().getHand());
    }

    @PostMapping("/surrender")
    public BlackjackDTO playerSurrender(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        var blackjackGame = blackJackService.playerSurrender(profile.getUsername());

        return new BlackjackDTO(
                blackjackGame.getGameState(),
                blackjackGame.getPlayer().getHand(),
                blackjackGame.getDealer().getHand());
    }

    @PostMapping("/double")
    public BlackjackDTO playerDouble(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();
        var blackjackGame = blackJackService.playerDouble(profile.getUsername());

        return new BlackjackDTO(
                blackjackGame.getGameState(),
                blackjackGame.getPlayer().getHand(),
                blackjackGame.getDealer().getHand());
    }
}
