package ao.kuilu.security;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();

        try {
            if (!jwtProvider.isTokenValid(token)) {
                return Mono.error(new RuntimeException("Token inválido"));
            }

            Claims claims = jwtProvider.parseToken(token);
            String userId = claims.getSubject();
            String role = (String) claims.get("role");

            List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + role)
            );

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userId,
                    token,
                    authorities
            );

            return Mono.just(auth);
        } catch (Exception e) {
            log.error("Erro ao autenticar: {}", e.getMessage());
            return Mono.error(e);
        }
    }
}
