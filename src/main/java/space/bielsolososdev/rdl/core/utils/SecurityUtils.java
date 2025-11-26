package space.bielsolososdev.rdl.core.utils;

import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.infrastructure.RdlProperties;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final RdlProperties properties;

    /**
     * Gera um token JWT para o usuário.
     */
    public String generateToken(String username) {
        return Jwts.builder()
            .subject(username)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + properties.getJwt().getExpiration()))
            .signWith(getSigningKey())
            .compact();
    }

    /**
     * Extrai o username do token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Valida se o token é válido para o usuário.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extrai a data de expiração do token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrai um claim específico do token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrai todos os claims do token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Verifica se o token expirou.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Obtém a chave de assinatura a partir do secret configurado.
     */
    private javax.crypto.SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getJwt().getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
