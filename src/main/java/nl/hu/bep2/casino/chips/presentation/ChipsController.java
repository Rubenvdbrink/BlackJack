package nl.hu.bep2.casino.chips.presentation;

import nl.hu.bep2.casino.chips.application.ChipsService;
import nl.hu.bep2.casino.chips.data.Chips;
import nl.hu.bep2.casino.chips.presentation.dto.BalanceDTO;
import nl.hu.bep2.casino.chips.presentation.dto.DepositDTO;
import nl.hu.bep2.casino.security.data.UserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/chips")
public class ChipsController {
    private final ChipsService service;

    public ChipsController(ChipsService service) {
        this.service = service;
    }

    @GetMapping("/balance")
    public BalanceDTO showBalance(Authentication authentication) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();

        Chips chips = this.service
                .findBalance(profile.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new BalanceDTO(
                chips.getUser().getUsername(),
                chips.getLastUpdate(),
                chips.getAmount()
        );
    }

    @PostMapping("/deposit")
    public void deposit(Authentication authentication, @RequestBody DepositDTO deposit) {
        UserProfile profile = (UserProfile) authentication.getPrincipal();

        this.service.depositChips(profile.getUsername(), deposit.amount);
    }
}
