package nl.hu.bep2.casino.blackjack.domain;

import java.io.Serializable;

public class Bet implements Serializable {
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
