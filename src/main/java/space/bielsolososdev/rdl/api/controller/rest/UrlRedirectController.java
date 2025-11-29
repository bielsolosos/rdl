package space.bielsolososdev.rdl.api.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.api.mapper.UrlRedirectMapper;
import space.bielsolososdev.rdl.api.model.urlredirect.UrlRedirectResponse;
import space.bielsolososdev.rdl.core.exception.BusinessException;
import space.bielsolososdev.rdl.core.exception.RedirectException;
import space.bielsolososdev.rdl.domain.url.service.UrlRedirectService;
@Tag(name = "Url Redirect")
@RestController
@RequestMapping("r")
@RequiredArgsConstructor
public class UrlRedirectController {

    private final UrlRedirectService service;

    @GetMapping("/{slug}")
    public void redirect(@PathVariable String slug, HttpServletResponse response) throws Exception {
        try {
            UrlRedirectResponse urlRedirect = UrlRedirectMapper.toUrlRedirectResponse(service.findBySlug(slug));
            response.sendRedirect(urlRedirect.url());
        } catch (BusinessException exception) {
            throw new RedirectException(exception.getMessage(), slug);
        }
    }
}
