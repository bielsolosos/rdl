package space.bielsolososdev.rdl.domain.users.service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.bielsolososdev.rdl.infrastructure.RdlProperties;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RdlProperties properties;
    private final Map<String, RefreshTokenData> tokens = new ConcurrentHashMap<>();

    public String createRefreshToken(String username) {
        String token = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now()
            .plusMillis(properties.getJwt().getRefreshExpiration());
        
        tokens.put(token, new RefreshTokenData(username, expiresAt));
        
        return token;
    }

    public String validateAndConsume(String token) {
        RefreshTokenData data = tokens.remove(token);
        
        if (data == null) {
            throw new IllegalArgumentException("Refresh token inválido ou já utilizado");
        }
        
        if (data.expiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Refresh token expirado");
        }
        
        return data.username();
    }

    public void cleanupExpiredTokens() {
        Instant now = Instant.now();
        long initialSize = tokens.size();
        tokens.entrySet().removeIf(entry -> entry.getValue().expiresAt().isBefore(now));
        long removed = initialSize - tokens.size();
        
        if (removed > 0) {
            log.debug("Removed {} expired refresh tokens", removed);
        }
    }

    private record RefreshTokenData(String username, Instant expiresAt) {}
}
