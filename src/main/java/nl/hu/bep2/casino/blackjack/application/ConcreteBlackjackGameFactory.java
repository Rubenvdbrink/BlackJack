package nl.hu.bep2.casino.blackjack.application;

import nl.hu.bep2.casino.blackjack.domain.*;
import org.springframework.stereotype.Component;

@Component
public class ConcreteBlackjackGameFactory implements BlackjackGameFactory {

    @Override
    public BlackjackGame create() {
        var handPlayer = new Hand();
        var player = new Player(handPlayer);

        var handDealer = new Hand();
        var dealer = new Dealer(new Deck(), handDealer, player);

        return new BlackjackGame(player, dealer);
    }
}
