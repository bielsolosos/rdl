package space.bielsolososdev.rdl.api.controller.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.api.mapper.UrlRedirectMapper;
import space.bielsolososdev.rdl.api.model.MessageResponse;
import space.bielsolososdev.rdl.api.model.urlredirect.UrlRedirectRequest;
import space.bielsolososdev.rdl.api.model.urlredirect.UrlRedirectResponse;
import space.bielsolososdev.rdl.core.exception.BusinessException;
import space.bielsolososdev.rdl.core.exception.RedirectException;
import space.bielsolososdev.rdl.domain.url.service.UrlRedirectService;

@RestController
@RequestMapping("redirect")
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

    @GetMapping()
    public ResponseEntity<List<UrlRedirectResponse>> listAll() {
        return ResponseEntity.ok(service.findAll().stream().map(UrlRedirectMapper::toUrlRedirectResponse).toList());
    }

    @PostMapping()
    public ResponseEntity<UrlRedirectResponse> createRedirect(@RequestBody UrlRedirectRequest request) {
        UrlRedirectResponse dto = UrlRedirectMapper
                .toUrlRedirectResponse(service.create(UrlRedirectMapper.toUrlRedirect(request)));

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UrlRedirectResponse> updateRedirect(
            @PathVariable Long id,
            @RequestBody UrlRedirectRequest request) {
        
        UrlRedirectResponse dto = UrlRedirectMapper
                .toUrlRedirectResponse(service.update(id, UrlRedirectMapper.toUrlRedirect(request)));

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRedirect(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new MessageResponse("Redirect deletado com sucesso"));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<UrlRedirectResponse> toggleEnabled(@PathVariable Long id) {
        UrlRedirectResponse dto = UrlRedirectMapper
                .toUrlRedirectResponse(service.toggleEnabled(id));

        return ResponseEntity.ok(dto);
    }
}
