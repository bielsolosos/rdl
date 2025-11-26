package space.bielsolososdev.rdl.infrastructure.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.bielsolososdev.rdl.domain.users.service.RefreshTokenService;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupScheduler {

    private final RefreshTokenService refreshTokenService;

    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredTokens() {
        log.debug("Iniciando limpeza de refresh tokens expirados");
        refreshTokenService.cleanupExpiredTokens();
    }
}
