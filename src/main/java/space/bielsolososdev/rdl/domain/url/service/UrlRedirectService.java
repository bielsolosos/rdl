package space.bielsolososdev.rdl.domain.url.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.core.exception.BusinessException;
import space.bielsolososdev.rdl.domain.url.model.UrlRedirect;
import space.bielsolososdev.rdl.domain.url.repository.UrlRedirectRepository;

@Service
@RequiredArgsConstructor
public class UrlRedirectService {

    private final UrlRedirectRepository repository;

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

    public UrlRedirect create(UrlRedirect entity) {
        entity.setCreatedAt(LocalDateTime.now());

        if(repository.findBySlug(entity.getSlug()).isPresent()) throw new BusinessException("Já existe um redirect com esse slug.");

        if(repository.findByUrl(entity.getUrl()).isPresent()) throw new BusinessException("Já existe um redirect com esse url.");

        return repository.save(entity);
    }

    public List<UrlRedirect> findAll() {
        return repository.findAll();
    }

    public UrlRedirect update(Long id, UrlRedirect updatedEntity) {
        UrlRedirect existingEntity = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Redirect não encontrado"));

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

        return repository.save(existingEntity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new BusinessException("Redirect não encontrado");
        }
        repository.deleteById(id);
    }

    public UrlRedirect toggleEnabled(Long id) {
        UrlRedirect entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Redirect não encontrado"));

        entity.setIsEnabled(!entity.getIsEnabled());
        entity.setUpdatedAt(LocalDateTime.now());

        return repository.save(entity);
    }

}
