package space.bielsolososdev.rdl.api.mapper;

import lombok.experimental.UtilityClass;
import space.bielsolososdev.rdl.api.model.UrlRedirectResponse;
import space.bielsolososdev.rdl.domain.url.model.UrlRedirect;

@UtilityClass
public class UrlRedirectMapper {

    public UrlRedirectResponse toUrlRedirectResponse(UrlRedirect entity) {
        return new UrlRedirectResponse(entity.getId(), entity.getSlug(), entity.getUrl());
    }
}
