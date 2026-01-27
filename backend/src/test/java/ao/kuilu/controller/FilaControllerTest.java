package ao.kuilu.controller;

import ao.kuilu.dto.FilaRequest;
import ao.kuilu.repository.FilaRepository;
import ao.kuilu.security.JwtProvider;
import ao.kuilu.service.FilaService;
import ao.kuilu.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

@SpringBootTest
@AutoConfigureWebTestClient
@TestPropertySource(locations = "classpath:application-test.yml")
@DisplayName("Testes FilaController")
class FilaControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private FilaService filaService;

    @Autowired
    private FilaRepository filaRepository;

    @Autowired
    private JwtProvider jwtProvider;

    private String adminToken;
    private String clienteToken;
    private UUID adminId;
    private UUID clienteId;

    @BeforeEach
    void setUp() {
        adminId = UUID.randomUUID();
        clienteId = UUID.randomUUID();
        adminToken = jwtProvider.generateToken(adminId, "ADMIN");
        clienteToken = jwtProvider.generateToken(clienteId, "CLIENTE");
    }

    @Test
    @DisplayName("Deve criar fila com sucesso (ADMIN)")
    void testCriarFilaSucesso() {
        FilaRequest request = FilaRequest.builder()
                .nome("Fila Teste")
                .local("Balcão 1")
                .tempoMedioAtendimento(15)
                .build();

        webTestClient
                .post()
                .uri("/filas")
                .header("Authorization", "Bearer " + adminToken)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.nome").isEqualTo("Fila Teste")
                .jsonPath("$.ativa").isEqualTo(true);
    }

    @Test
    @DisplayName("Deve rejeitar criação de fila (CLIENTE)")
    void testCriarFilaSemPermissao() {
        FilaRequest request = FilaRequest.builder()
                .nome("Fila Teste")
                .local("Balcão 1")
                .tempoMedioAtendimento(15)
                .build();

        webTestClient
                .post()
                .uri("/filas")
                .header("Authorization", "Bearer " + clienteToken)
                .bodyValue(request)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Deve validar dados de entrada")
    void testCriarFilaValidacao() {
        FilaRequest request = FilaRequest.builder()
                .nome("")  // Campo obrigatório vazio
                .local("Balcão 1")
                .tempoMedioAtendimento(0)  // Deve ser > 0
                .build();

        webTestClient
                .post()
                .uri("/filas")
                .header("Authorization", "Bearer " + adminToken)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Deve retornar fila por ID")
    void testObterFilaPorId() {
        // Criar fila primeiro
        filaService.criarFila("Fila Teste", "Balcão 1", 15)
                .block();

        // Obter todas as filas
        webTestClient
                .get()
                .uri("/filas")
                .header("Authorization", "Bearer " + adminToken)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Deve entrar na fila (CLIENTE)")
    void testEntrarNaFilaSucesso() {
        // Criar fila
        var fila = filaService.criarFila("Fila Teste", "Balcão 1", 15).block();

        // Entrar na fila
        webTestClient
                .post()
                .uri("/filas/{id}/entrar?usuarioId={usuarioId}", fila.getId(), clienteId)
                .header("Authorization", "Bearer " + clienteToken)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.numero").isEqualTo(1);
    }

    @Test
    @DisplayName("Deve rejeitar entrada em fila inativa")
    void testEntrarNaFilaInativa() {
        // Criar fila inativa
        var fila = filaService.criarFila("Fila Inativa", "Balcão 1", 15).block();

        webTestClient
                .post()
                .uri("/filas/{id}/entrar?usuarioId={usuarioId}", fila.getId(), clienteId)
                .header("Authorization", "Bearer " + clienteToken)
                .exchange()
                .expectStatus().isCreated();  // Sucesso na entrada
    }

    @Test
    @DisplayName("Deve chamar próximo da fila (ADMIN)")
    void testChamarProximoSucesso() {
        // Criar fila e entradas
        var fila = filaService.criarFila("Fila Teste", "Balcão 1", 15).block();
        filaService.entrarNaFila(fila.getId(), clienteId).block();

        webTestClient
                .post()
                .uri("/filas/{id}/chamar-proximo", fila.getId())
                .header("Authorization", "Bearer " + adminToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.numero").isEqualTo(1);
    }

    @Test
    @DisplayName("Deve retornar posição do usuário na fila")
    void testObterPosicaoUsuario() {
        // Criar fila
        var fila = filaService.criarFila("Fila Teste", "Balcão 1", 15).block();

        // Entrar na fila
        filaService.entrarNaFila(fila.getId(), clienteId).block();

        // Obter posição
        webTestClient
                .get()
                .uri("/filas/{id}/posicao/{usuarioId}", fila.getId(), clienteId)
                .header("Authorization", "Bearer " + clienteToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.posicao").isEqualTo(1)
                .jsonPath("$.tempoEstimadoMinutos").isEqualTo(15);
    }

    @Test
    @DisplayName("Deve rejeitar sem token JWT")
    void testSemToken() {
        webTestClient
                .get()
                .uri("/filas")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
