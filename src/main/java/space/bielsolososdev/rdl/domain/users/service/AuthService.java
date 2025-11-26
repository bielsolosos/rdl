package space.bielsolososdev.rdl.domain.users.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.api.model.auth.LoginRequest;
import space.bielsolososdev.rdl.api.model.auth.RefreshRequest;
import space.bielsolososdev.rdl.api.model.auth.TokenResponse;
import space.bielsolososdev.rdl.core.utils.SecurityUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final SecurityUtils jwtUtil;
    private final RefreshTokenService refreshTokenService;

    /**
     * Realiza autenticação e retorna tokens JWT.
     * 
     * @throws BadCredentialsException se usuário ou senha estiverem incorretos
     * @throws DisabledException       se a conta estiver desabilitada
     * @throws LockedException         se a conta estiver bloqueada
     */
    public TokenResponse login(LoginRequest request) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        String token = jwtUtil.generateToken(request.username());
        String refreshToken = refreshTokenService.createRefreshToken(request.username());

        return new TokenResponse(token, refreshToken);
    }

    public TokenResponse refresh(RefreshRequest request) {
        String username = refreshTokenService.validateAndConsume(request.refreshToken());

        return new TokenResponse(jwtUtil.generateToken(username), refreshTokenService.createRefreshToken(username));
    }

}
