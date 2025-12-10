package space.bielsolososdev.rdl.api.controller.web;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.api.mapper.UrlRedirectMapper;
import space.bielsolososdev.rdl.api.model.urlredirect.UrlRedirectRequest;
import space.bielsolososdev.rdl.domain.url.model.UrlRedirect;
import space.bielsolososdev.rdl.domain.url.service.UrlRedirectService;

@Controller
@RequestMapping("/urls")
@RequiredArgsConstructor
public class UrlManagementController {

    private final UrlRedirectService service;

    @GetMapping
    public String listUrls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) Boolean isEnabled,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<UrlRedirect> urls = service.listMyUrls(pageable, filter, isEnabled, createdAfter, createdBefore);

        model.addAttribute("urls", urls);
        model.addAttribute("filter", filter);
        model.addAttribute("isEnabled", isEnabled);
        model.addAttribute("createdAfter", createdAfter);
        model.addAttribute("createdBefore", createdBefore);

        return "urls/list";
    }

    @PostMapping
    public String createUrl(
            @RequestParam String slug,
            @RequestParam String url,
            RedirectAttributes redirectAttributes) {
        try {
            UrlRedirectRequest request = new UrlRedirectRequest(slug, url, true);
            service.create(UrlRedirectMapper.toUrlRedirect(request));
            redirectAttributes.addFlashAttribute("success", "URL criada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/urls";
    }

    @PostMapping("/{id}/delete")
    public String deleteUrl(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("success", "URL deletada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/urls";
    }

    @PostMapping("/{id}/toggle")
    public String toggleUrl(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.toggleEnabled(id);
            redirectAttributes.addFlashAttribute("success", "Status alterado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/urls";
    }

    @PostMapping("/{id}/edit")
    public String editUrl(
            @PathVariable Long id,
            @RequestParam String slug,
            @RequestParam String url,
            @RequestParam(defaultValue = "true") Boolean isEnabled,
            RedirectAttributes redirectAttributes) {
        try {
            UrlRedirectRequest request = new UrlRedirectRequest(slug, url, isEnabled);
            service.update(id, UrlRedirectMapper.toUrlRedirect(request));
            redirectAttributes.addFlashAttribute("success", "URL atualizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/urls";
    }
}
