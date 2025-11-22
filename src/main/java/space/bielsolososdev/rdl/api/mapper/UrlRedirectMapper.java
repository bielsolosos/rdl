package space.bielsolososdev.rdl.api.mapper;

import lombok.experimental.UtilityClass;
import space.bielsolososdev.rdl.api.model.urlredirect.UrlRedirectRequest;
import space.bielsolososdev.rdl.api.model.urlredirect.UrlRedirectResponse;
import space.bielsolososdev.rdl.domain.url.model.UrlRedirect;

@UtilityClass
public class UrlRedirectMapper {

    public UrlRedirectResponse toUrlRedirectResponse(UrlRedirect entity) {
        return new UrlRedirectResponse(entity.getId(), entity.getSlug(), entity.getUrl(), entity.getIsEnabled());
    }

    public UrlRedirect toUrlRedirect(UrlRedirectRequest dto) {
        UrlRedirect entity = new UrlRedirect();

        entity.setSlug(dto.slug());
        entity.setUrl(dto.url());
        entity.setIsEnabled(dto.isEnabled());

        return entity;
    }
}
