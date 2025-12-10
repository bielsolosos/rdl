package space.bielsolososdev.rdl.domain.url.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import space.bielsolososdev.rdl.domain.url.model.UrlRedirect;

public interface UrlRedirectRepository extends JpaRepository<UrlRedirect, Long>, JpaSpecificationExecutor<UrlRedirect> {

    /**
     * Busca uma URL pelo slug que esteja habilitada
     */
    Optional<UrlRedirect> findBySlugAndIsEnabledTrue(String slug);
    
    /**
     * Busca uma URL pelo slug (independente se está habilitada)
     */
    Optional<UrlRedirect> findBySlug(String slug);

    /**
     * Busca uma URL pelo url (independente se está habilitada)
     */
    Optional<UrlRedirect> findByUrl(String url);
    
    /**
     * Verifica se existe um slug
     */
    boolean existsBySlug(String slug);
}
