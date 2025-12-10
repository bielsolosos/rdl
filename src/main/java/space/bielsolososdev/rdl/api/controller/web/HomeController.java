package space.bielsolososdev.rdl.api.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.domain.dashboard.service.DashboardService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final DashboardService dashboardService;

    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false) String error) {
        var stats = dashboardService.getDashboardStats();

        model.addAttribute("appName", stats.appName());
        model.addAttribute("message", stats.message());
        model.addAttribute("timestamp", stats.startedAt());
        model.addAttribute("totalUrls", stats.urlStats().total());
        model.addAttribute("enabledUrls", stats.urlStats().enabled());
        model.addAttribute("disabledUrls", stats.urlStats().disabled());
        model.addAttribute("enabledPercentage", String.format("%.1f%%", stats.urlStats().enabledPercentage()));
        model.addAttribute("database", stats.systemInfo().database());
        model.addAttribute("javaVersion", stats.systemInfo().javaVersion());
        model.addAttribute("framework", stats.systemInfo().frameworkVersion());

        // Adiciona mensagem de erro se houver (ex: acesso negado)
        if ("forbidden".equals(error)) {
            model.addAttribute("errorMessage", "Acesso negado! Você não tem permissão para acessar essa área.");
        }

        return "index";
    }

    @GetMapping("/health")
    public String health(Model model) {
        model.addAttribute("status", "UP");
        model.addAttribute("database", "PostgreSQL");
        model.addAttribute("timestamp", java.time.LocalDateTime.now());
        return "health";
    }
}
