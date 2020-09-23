package nl.hu.bep2.casino.blackjack.domain;

import org.springframework.stereotype.Component;

@Component
public class Bet {
    private Long amount;

    public Bet() {}
    public Bet(Long amount) {
        this.amount = amount;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}