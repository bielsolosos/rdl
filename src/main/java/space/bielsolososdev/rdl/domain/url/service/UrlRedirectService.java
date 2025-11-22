package space.bielsolososdev.rdl.domain.url.service;

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

}
