package space.bielsolososdev.rdl.api.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.api.mapper.UrlRedirectMapper;
import space.bielsolososdev.rdl.api.model.urlredirect.UrlRedirectRequest;
import space.bielsolososdev.rdl.domain.url.service.UrlRedirectService;

@Controller
@RequestMapping("/urls")
@RequiredArgsConstructor
public class UrlManagementController {

    private final UrlRedirectService service;

    @GetMapping
    public String listUrls(Model model) {
        model.addAttribute("urls", service.findAll());
        return "urls/list";
    }

    @PostMapping
    public String createUrl(@RequestParam String slug, 
                           @RequestParam String url,
                           Model model) {
        try {
            UrlRedirectRequest request = new UrlRedirectRequest(slug, url, true);
            service.create(UrlRedirectMapper.toUrlRedirect(request));
            model.addAttribute("success", "URL criada com sucesso!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("urls", service.findAll());
        return "urls/list";
    }

    @PostMapping("/{id}/delete")
    public String deleteUrl(@PathVariable Long id, Model model) {
        try {
            service.delete(id);
            model.addAttribute("success", "URL deletada com sucesso!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("urls", service.findAll());
        return "urls/list";
    }

    @PostMapping("/{id}/toggle")
    public String toggleUrl(@PathVariable Long id, Model model) {
        try {
            service.toggleEnabled(id);
            model.addAttribute("success", "Status alterado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("urls", service.findAll());
        return "urls/list";
    }
}
