package space.bielsolososdev.rdl.api.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.core.utils.SecurityUtils;
import space.bielsolososdev.rdl.domain.users.service.RefreshTokenService;

/**
 * Controller temporário com a lógica de token toda interna dentro dele. Falta colocar tudo certinho integrando a lógica da service depois.
 * TODO: RESOLVER ISSO DAQUI.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final SecurityUtils jwtUtil;
    private final RefreshTokenService refreshTokenService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            
            String token = jwtUtil.generateToken(request.username());
            String refreshToken = refreshTokenService.createRefreshToken(request.username());
            return ResponseEntity.ok(new TokenResponse(token, refreshToken));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        try {
            String username = refreshTokenService.validateAndConsume(request.refreshToken());
            
            String newToken = jwtUtil.generateToken(username);
            String newRefreshToken = refreshTokenService.createRefreshToken(username);
            return ResponseEntity.ok(new TokenResponse(newToken, newRefreshToken));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Refresh token inválido");
        }
    }
}

record LoginRequest(String username, String password) {}
record RefreshRequest(String refreshToken) {}
record TokenResponse(String token, String refreshToken) {}