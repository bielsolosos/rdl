package space.bielsolososdev.rdl.api.model.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank(message = "É obrigatório ter um refresh token") String refreshToken) {
}
