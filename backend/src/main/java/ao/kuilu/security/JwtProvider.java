package ao.kuilu.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtProvider {

    @Value("${security.jwt.secret:mySuperSecretKeyThatIsAtLeast256BitsLongForHS256AlgorithmOkay}")
    private String jwtSecret;

    @Value("${security.jwt.expiration:86400000}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Gera JWT token
     */
    public String generateToken(UUID userId, String role) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

            return Jwts.builder()
                    .subject(userId.toString())
                    .claim("role", role)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error("Erro ao gerar JWT: {}", e.getMessage());
            throw new RuntimeException("Erro ao gerar token JWT");
        }
    }

    /**
     * Valida e extrai claims do token
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token JWT inválido: {}", e.getMessage());
            throw new RuntimeException("Token JWT inválido");
        }
    }

    /**
     * Extrai userId do token
     */
    public UUID getUserIdFromToken(String token) {
        return UUID.fromString(parseToken(token).getSubject());
    }

    /**
     * Extrai role do token
     */
    public String getRoleFromToken(String token) {
        return (String) parseToken(token).get("role");
    }

    /**
     * Valida se token ainda é válido
     */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token inválido: {}", e.getMessage());
            return false;
        }
    }
}
