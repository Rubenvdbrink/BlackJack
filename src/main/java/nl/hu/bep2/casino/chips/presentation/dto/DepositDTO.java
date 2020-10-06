package nl.hu.bep2.casino.chips.presentation.dto;

import javax.validation.constraints.Positive;

public class DepositDTO {
    @Positive
    public Long amount;
}
