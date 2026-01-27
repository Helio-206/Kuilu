package ao.kuilu.controller;

import ao.kuilu.dto.LoginRequest;
import ao.kuilu.dto.TokenResponse;
import ao.kuilu.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticação", description = "Endpoints de autenticação JWT")
public class AuthController {

    private final JwtProvider jwtProvider;

    /**
     * POST /auth/login
     * Simula autenticação e gera token JWT
     */
    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Gera token JWT para acesso à API")
    public Mono<ResponseEntity<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login solicitado para usuário: {}", request.getUsuarioId());

        try {
            String token = jwtProvider.generateToken(request.getUsuarioId(), request.getRole().name());

            TokenResponse response = TokenResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .usuarioId(request.getUsuarioId())
                    .role(request.getRole().name())
                    .build();

            return Mono.just(ResponseEntity.ok(response));
        } catch (Exception e) {
            log.error("Erro ao gerar token: {}", e.getMessage());
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }
}
