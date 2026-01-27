# Kuilu Backend

Sistema de gestão de filas reativo com Spring Boot WebFlux.

## 🚀 Stack

- **Java**: 21
- **Framework**: Spring Boot 3.2.1
- **Reatividade**: Spring WebFlux
- **Banco de Dados**: PostgreSQL com R2DBC
- **Segurança**: JWT (stateless)
- **Validação**: Jakarta Validation
- **Lombok**: Redução de boilerplate
- **API Documentation**: OpenAPI/Swagger
- **Testes**: WebTestClient + JUnit 5

## 📁 Estrutura

```
ao.kuilu
├── config/              # Configurações (Security, OpenAPI, etc)
├── controller/          # API REST endpoints
├── domain/
│   ├── model/          # Entidades (Fila, Usuario, EntradaFila)
│   └── enums/          # Enums (Role)
├── dto/                # Data Transfer Objects
├── repository/         # Data access layer (R2DBC)
├── security/           # JWT handling (JwtProvider, JwtTokenConverter, etc)
└── service/            # Business logic (FilaService)
```

## 🔧 Configuração

### Variáveis de Ambiente

```bash
DATABASE_URL=r2dbc:postgresql://localhost:5432/kuilu_db
DATABASE_USER=postgres
DATABASE_PASSWORD=postgres
JWT_SECRET=mySuperSecretKeyThatIsAtLeast256BitsLongForHS256AlgorithmOkay
JWT_EXPIRATION=86400000  # 24 horas em ms
```

### Build

```bash
mvn clean package
```

### Executar

```bash
mvn spring-boot:run
```

### Testes

```bash
mvn test
```

## 🔐 Autenticação JWT

Todos os endpoints (exceto docs) requerem token JWT.

### Usar Token

```bash
curl -H "Authorization: Bearer seu-token-jwt" \
  http://localhost:8080/api/filas
```

## 📚 API Endpoints

| Método | Endpoint | Permissão | Descrição |
|--------|----------|-----------|-----------|
| POST | `/filas` | ADMIN | Criar fila |
| GET | `/filas/{id}` | Público | Obter detalhes |
| POST | `/filas/{id}/entrar` | CLIENTE | Entrar na fila |
| POST | `/filas/{id}/chamar-proximo` | ADMIN | Chamar próximo |
| GET | `/filas/{id}/posicao/{usuarioId}` | CLIENTE | Obter posição |

### Documentação Interativa

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 🧪 Testes

```bash
mvn test
```

## 📝 Regras de Negócio

- ✅ Usuário não pode entrar duplicado na mesma fila
- ✅ Fila inativa não permite entrada
- ✅ Número sequencial é atômico
- ✅ Tempo estimado = posição × tempo médio
- ✅ ADMIN cria filas e chama próximo
- ✅ CLIENTE entra e visualiza posição
