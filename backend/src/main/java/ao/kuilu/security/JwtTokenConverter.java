package ao.kuilu.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtTokenConverter implements ServerAuthenticationConverter {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("Authorization"))
                .filter(auth -> auth.startsWith("Bearer "))
                .map(auth -> auth.substring(7))
                .flatMap(token -> {
                    if (jwtProvider.isTokenValid(token)) {
                        return Mono.just((Authentication) new UsernamePasswordAuthenticationToken(token, token));
                    }
                    return Mono.empty();
                })
                .onErrorResume(e -> {
                    log.error("Erro ao converter token: {}", e.getMessage());
                    return Mono.empty();
                });
    }
}
