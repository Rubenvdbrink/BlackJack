package nl.hu.bep2.casino.blackjack.application;

import nl.hu.bep2.casino.blackjack.domain.*;
import org.springframework.stereotype.Component;

@Component
public class ConcreteBlackjackGameFactory implements BlackjackGameFactory {

    @Override
    public BlackjackGame create(Long bet) {
        var handPlayer = new Hand();
        var player = new Player(handPlayer);

        var handDealer = new Hand();
        var dealer = new Dealer(new Deck(), handDealer, player);

        BlackjackGame blackjackGame = new BlackjackGame(player, dealer);
        blackjackGame.setBet(new Bet(bet));

        return blackjackGame;
    }
}
