package nl.hu.bep2.casino.security.presentation.dto;

import javax.validation.constraints.NotBlank;

public class LoginDTO {
    @NotBlank
    public String username;

    @NotBlank
    public String password;
}
