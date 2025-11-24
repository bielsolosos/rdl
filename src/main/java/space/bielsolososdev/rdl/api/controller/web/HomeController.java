package space.bielsolososdev.rdl.api.controller.web;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.domain.url.service.UrlRedirectService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UrlRedirectService urlRedirectService;

    @GetMapping("/")
    public String home(Model model) {
        //TODO tirar essa lógica do controller. 
        var allUrls = urlRedirectService.findAll();
        long totalUrls = allUrls.size();
        long enabledUrls = allUrls.stream().filter(url -> url.getIsEnabled()).count();
        long disabledUrls = totalUrls - enabledUrls;

        model.addAttribute("appName", "Redirect Lab");
        model.addAttribute("message", "Aplicação funcionando perfeitamente! 🚀");
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("totalUrls", totalUrls);
        model.addAttribute("enabledUrls", enabledUrls);
        model.addAttribute("disabledUrls", disabledUrls);
        return "index";
    }

    @GetMapping("/health")
    public String health(Model model) {
        model.addAttribute("status", "UP");
        model.addAttribute("database", "PostgreSQL");
        model.addAttribute("timestamp", LocalDateTime.now());
        return "health";
    }
}
