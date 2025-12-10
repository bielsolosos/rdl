package space.bielsolososdev.rdl.domain.url.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.bielsolososdev.rdl.core.exception.BusinessException;
import space.bielsolososdev.rdl.domain.url.model.UrlRedirect;
import space.bielsolososdev.rdl.domain.url.repository.UrlRedirectRepository;
import space.bielsolososdev.rdl.domain.url.repository.specification.UrlRedirectSpecification;
import space.bielsolososdev.rdl.domain.users.model.User;
import space.bielsolososdev.rdl.domain.users.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlRedirectService {

    private final UrlRedirectRepository repository;
    private final UserService userService;

    @Cacheable(value = "urls", key = "#slug")
    public UrlRedirect findBySlug(String slug) {
        Optional<UrlRedirect> entity = repository.findBySlug(slug);

        if (entity.isEmpty()) {
            throw new BusinessException("Slug não existe dentro do sistema");
        }

        if (!entity.get().getIsEnabled()) {
            throw new BusinessException("Redirect desabilitado do sistema.");
        }

        return entity.get();
    }

    @CacheEvict(value = "urls", key = "#entity.slug")
    public UrlRedirect create(UrlRedirect entity) {
        User user = userService.getMe();
        
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUser(user);

        if (repository.findBySlug(entity.getSlug()).isPresent()) {
            log.warn("Tentativa de criar redirect com slug já existente: {}", entity.getSlug());
            throw new BusinessException("Já existe um redirect com esse slug.");
        }

        if (repository.findByUrl(entity.getUrl()).isPresent()) {
            log.warn("Tentativa de criar redirect com URL já existente: {}", entity.getUrl());
            throw new BusinessException("Já existe um redirect com esse url.");
        }

        UrlRedirect savedEntity = repository.save(entity);
        log.info("✅ Redirect criado com sucesso: slug='{}' -> '{}' (User: {})", 
                savedEntity.getSlug(), savedEntity.getUrl(), user.getUsername());

        return savedEntity;
    }

    public Page<UrlRedirect> listUrls(
            Pageable pageable,
            String filter,
            Boolean isEnabled,
            Long userId,
            LocalDateTime createdAfter,
            LocalDateTime createdBefore) {

        UrlRedirectSpecification spec = new UrlRedirectSpecification(
                filter,
                isEnabled,
                userId,
                createdAfter,
                createdBefore);

        return repository.findAll(spec, pageable);
    }

    public Page<UrlRedirect> listMyUrls(
            Pageable pageable,
            String filter,
            Boolean isEnabled,
            LocalDateTime createdAfter,
            LocalDateTime createdBefore) {

        User user = userService.getMe();
        
        return listUrls(pageable, filter, isEnabled, user.getId(), createdAfter, createdBefore);
    }

    @CacheEvict(value = "urls", allEntries = true)
    public UrlRedirect update(Long id, UrlRedirect updatedEntity) {
        User user = userService.getMe();
        
        UrlRedirect existingEntity = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Redirect não encontrado"));

        // Verifica se o redirect pertence ao usuário
        if (!existingEntity.getUser().getId().equals(user.getId())) {
            log.warn("Usuário {} tentou editar redirect que não lhe pertence (ID: {})", 
                    user.getUsername(), id);
            throw new BusinessException("Você não tem permissão para editar este redirect");
        }

        repository.findBySlug(updatedEntity.getSlug())
                .ifPresent(entity -> {
                    if (!entity.getId().equals(id)) {
                        throw new BusinessException("Já existe um redirect com esse slug.");
                    }
                });

        repository.findByUrl(updatedEntity.getUrl())
                .ifPresent(entity -> {
                    if (!entity.getId().equals(id)) {
                        throw new BusinessException("Já existe um redirect com esse url.");
                    }
                });

        existingEntity.setSlug(updatedEntity.getSlug());
        existingEntity.setUrl(updatedEntity.getUrl());
        existingEntity.setIsEnabled(updatedEntity.getIsEnabled());
        existingEntity.setUpdatedAt(LocalDateTime.now());

        UrlRedirect savedEntity = repository.save(existingEntity);
        log.info("Redirect atualizado: slug='{}' -> '{}' (User: {})", 
                savedEntity.getSlug(), savedEntity.getUrl(), user.getUsername());

        return savedEntity;
    }

    @CacheEvict(value = "urls", allEntries = true)
    public void delete(Long id) {
        User user = userService.getMe();
        
        UrlRedirect entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Redirect não encontrado"));

        // Verifica se o redirect pertence ao usuário
        if (!entity.getUser().getId().equals(user.getId())) {
            log.warn("Usuário {} tentou deletar redirect que não lhe pertence (ID: {})", 
                    user.getUsername(), id);
            throw new BusinessException("Você não tem permissão para deletar este redirect");
        }

        repository.deleteById(id);
        log.info("Redirect deletado: slug='{}' (User: {})", entity.getSlug(), user.getUsername());
    }

    @CacheEvict(value = "urls", allEntries = true)
    public UrlRedirect toggleEnabled(Long id) {
        User user = userService.getMe();
        
        UrlRedirect entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Redirect não encontrado"));

        // Verifica se o redirect pertence ao usuário
        if (!entity.getUser().getId().equals(user.getId())) {
            log.warn("Usuário {} tentou alterar status de redirect que não lhe pertence (ID: {})", 
                    user.getUsername(), id);
            throw new BusinessException("Você não tem permissão para alterar este redirect");
        }

        entity.setIsEnabled(!entity.getIsEnabled());
        entity.setUpdatedAt(LocalDateTime.now());

        UrlRedirect savedEntity = repository.save(entity);
        log.info("Status do redirect '{}' alterado para: {} (User: {})", 
                savedEntity.getSlug(), savedEntity.getIsEnabled() ? "HABILITADO" : "DESABILITADO", 
                user.getUsername());

        return savedEntity;
    }

}
