package space.bielsolososdev.rdl.api.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.api.model.auth.LoginRequest;
import space.bielsolososdev.rdl.api.model.auth.RefreshRequest;
import space.bielsolososdev.rdl.api.model.auth.TokenResponse;
import space.bielsolososdev.rdl.domain.users.service.AuthService;

/**
 * Controller temporário com a lógica de token toda interna dentro dele. Falta
 * colocar tudo certinho integrando a lógica da service depois.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok().body(service.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok().body(service.refresh(request));
    }
}
