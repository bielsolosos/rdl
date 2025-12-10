package space.bielsolososdev.rdl.api.controller.rest;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.api.mapper.UrlRedirectMapper;
import space.bielsolososdev.rdl.api.model.MessageResponse;
import space.bielsolososdev.rdl.api.model.urlredirect.UrlRedirectRequest;
import space.bielsolososdev.rdl.api.model.urlredirect.UrlRedirectResponse;
import space.bielsolososdev.rdl.domain.url.service.UrlRedirectService;

@Tag(name = "Redirects", description = "Gerenciamento de URLs encurtadas e redirects")
@RestController
@RequestMapping("api/redirect")
@RequiredArgsConstructor
public class UrlRedirectOperationsController {

    private final UrlRedirectService service;

    @Operation(summary = "Listar redirects com paginação e filtros", description = "Retorna uma lista paginada de redirects do usuário autenticado com opções de filtro")
    @GetMapping()
    public ResponseEntity<Page<UrlRedirectResponse>> listAll(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) Boolean isEnabled,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore) {
        Page<UrlRedirectResponse> response = service
                .listMyUrls(pageable, filter, isEnabled, createdAfter, createdBefore)
                .map(UrlRedirectMapper::toUrlRedirectResponse);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Criar novo redirect", description = "Cria um novo redirect associado ao usuário autenticado")
    @PostMapping()
    public ResponseEntity<UrlRedirectResponse> createRedirect(@RequestBody UrlRedirectRequest request) {
        UrlRedirectResponse dto = UrlRedirectMapper
                .toUrlRedirectResponse(service.create(UrlRedirectMapper.toUrlRedirect(request)));

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Atualizar redirect existente", description = "Atualiza um redirect do usuário autenticado")
    @PutMapping("/{id}")
    public ResponseEntity<UrlRedirectResponse> updateRedirect(
            @PathVariable Long id,
            @RequestBody UrlRedirectRequest request) {

        UrlRedirectResponse dto = UrlRedirectMapper
                .toUrlRedirectResponse(service.update(id, UrlRedirectMapper.toUrlRedirect(request)));

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Deletar redirect", description = "Deleta um redirect do usuário autenticado")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRedirect(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new MessageResponse("Redirect deletado com sucesso"));
    }

    @Operation(summary = "Alternar status do redirect", description = "Habilita ou desabilita um redirect do usuário autenticado")
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<UrlRedirectResponse> toggleEnabled(@PathVariable Long id) {
        UrlRedirectResponse dto = UrlRedirectMapper
                .toUrlRedirectResponse(service.toggleEnabled(id));

        return ResponseEntity.ok(dto);
    }
}
