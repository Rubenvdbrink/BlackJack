package nl.hu.bep2.casino.blackjack.presentation.dto;

import javax.validation.constraints.Positive;

public class BetDTO {
    @Positive
    public Long betAmount;
}
