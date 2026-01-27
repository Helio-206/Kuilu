# Kuilu Backend

API Reativa com Spring Boot WebFlux

## Estrutura

```
src/
├── main/
│   ├── java/com/kuilu/
│   │   ├── config/         # Configurações da aplicação
│   │   ├── controller/     # Controllers REST
│   │   ├── service/        # Lógica de negócio
│   │   ├── repository/     # Acesso a dados (R2DBC)
│   │   ├── model/          # Entidades e enums
│   │   └── KuiluBackendApplication.java
│   └── resources/
│       └── application.yml # Configurações
└── test/
    └── java/com/kuilu/     # Testes automatizados
```

## Configuração

### Variáveis de Ambiente

```bash
DATABASE_URL=r2dbc:postgresql://localhost:5432/kuilu_db
DATABASE_USER=postgres
DATABASE_PASSWORD=postgres
```

### Build e Execução

```bash
# Build
mvn clean package

# Executar
mvn spring-boot:run

# Testes
mvn test
```

## API

- Base URL: `http://localhost:8080/api`
- Health Check: `http://localhost:8080/actuator/health`

## Dependências

- Spring Boot 3.2.1
- Java 17
- Spring WebFlux
- Spring Data R2DBC
- PostgreSQL
- Spring Security
- Lombok
