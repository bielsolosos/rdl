package space.bielsolososdev.rdl.api.model.urlredirect;

import jakarta.validation.constraints.NotBlank;

public record UrlRedirectRequest(
    
    @NotBlank(message = "Slug é obrigatório.")
    String slug,

    @NotBlank(message = "url é obrigatório.")
    String url,

    boolean isEnabled
) {

}
