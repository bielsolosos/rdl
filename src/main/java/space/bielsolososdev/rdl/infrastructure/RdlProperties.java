package space.bielsolososdev.rdl.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "rdl")
public class RdlProperties {

    private Jwt jwt;

    @Data
    public static class Jwt{

        private String secret;

        private Long expiration;
        
        private Long refreshExpiration;
    }
}
