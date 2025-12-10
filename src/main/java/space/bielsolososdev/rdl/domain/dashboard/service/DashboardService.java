package space.bielsolososdev.rdl.domain.dashboard.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.bielsolososdev.rdl.api.model.DashboardStatsResponse;
import space.bielsolososdev.rdl.api.model.DashboardStatsResponse.SystemInfo;
import space.bielsolososdev.rdl.api.model.DashboardStatsResponse.UrlStats;
import space.bielsolososdev.rdl.core.config.StartupTimeListener;
import space.bielsolososdev.rdl.domain.url.service.UrlRedirectService;
import space.bielsolososdev.rdl.domain.users.model.User;
import space.bielsolososdev.rdl.domain.users.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UrlRedirectService urlRedirectService;
    private final StartupTimeListener timeListener;
    private final UserRepository userRepository;

    /**
     * Retorna estatísticas do dashboard.
     * Se houver usuário autenticado, mostra apenas suas URLs.
     * Se for público (não autenticado), mostra estatísticas globais.
     */
    public DashboardStatsResponse getDashboardStats() {
        log.debug("Gerando estatísticas do dashboard");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null 
                && authentication.isAuthenticated() 
                && !"anonymousUser".equals(authentication.getPrincipal());

        Long userId = null;
        String username = "Visitante";

        if (isAuthenticated) {
            String authUsername = authentication.getName();
            User user = userRepository.findByUsername(authUsername).orElse(null);
            if (user != null) {
                userId = user.getId();
                username = user.getUsername();
                log.debug("Dashboard acessado por usuário autenticado: {}", username);
            }
        } else {
            log.debug("Dashboard acessado por visitante não autenticado");
        }

        var urls = urlRedirectService.listUrls(
                PageRequest.of(0, Integer.MAX_VALUE),
                null, 
                null, 
                userId, // null = todas as URLs (público), ou ID do usuário (autenticado)
                null, 
                null);

        long total = urls.getTotalElements();
        long enabled = urls.getContent().stream()
                .filter(url -> url.getIsEnabled())
                .count();
        long disabled = total - enabled;
        double enabledPercentage = total > 0 ? (enabled * 100.0 / total) : 0.0;

        UrlStats urlStats = new UrlStats(total, enabled, disabled, enabledPercentage);

        SystemInfo systemInfo = new SystemInfo(
                "PostgreSQL 16",
                System.getProperty("java.version"),
                "Spring Boot 3.5.8");

        String message = isAuthenticated 
                ? "Bem-vindo ao seu painel de controle! 🚀"
                : "Sistema de gerenciamento de URLs 🔗";

        log.info("Dashboard stats geradas: {} URLs totais ({} ativas, {} inativas) - User: {}",
                total, enabled, disabled, username);

        return new DashboardStatsResponse(
                "Redirect Lab",
                message,
                timeListener.getStartedAt(),
                urlStats,
                systemInfo);
    }
}
