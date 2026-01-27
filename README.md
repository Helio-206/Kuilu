# Kuilu

**Sua vez, sem perder tempo** 

Plataforma digital de gestão de filas físicas em Angola. Simples, rápida e com baixo consumo de dados.

##  Sobre o Projeto

Kuilu é um sistema que permite:
- Usuários entrar em filas virtuais via web/mobile
- Estabelecimentos gerenciar chamadas e atendimentos
- Visualizar posição e tempo estimado de espera
- Funcionar offline com suporte PWA

##  Stack

### Backend
- **Java 21** com Spring Boot 3.2.1
- **Spring WebFlux** (reativo)
- **PostgreSQL** com R2DBC
- **JWT** para autenticação stateless
- **OpenAPI/Swagger** para documentação

### Frontend
- **Next.js 14+** com TypeScript
- **Tailwind CSS** para estilo
- **PWA** para mobile
- **Mobile-first** design

##  Quick Start

### 1. Clonar o repositório

```bash
git clone https://github.com/Helio-206/Kuilu.git
cd Kuilu
```

### 2. Instalar dependências

```bash
npm install
```

Isso instalará concurrently no root e preparará o projeto.

### 3. Iniciar PostgreSQL (Docker)

```bash
docker-compose up -d
```

Acesse o Adminer em: http://localhost:8081
- Servidor: postgres
- Usuário: postgres
- Senha: postgres
- Database: kuilu_db

### 4. Rodar Frontend e Backend simultaneamente

```bash
npm run dev
```

Isso iniciará:
- **Backend**: http://localhost:8080/api
  - Swagger: http://localhost:8080/swagger-ui.html
- **Frontend**: http://localhost:3000

### 5. (Opcional) Instalar todas as dependências Maven

```bash
npm run install:all
```

##  Estrutura do Projeto

```
Kuilu/
├── backend/              # Spring Boot WebFlux API
│   ├── pom.xml
│   └── src/main/java/ao/kuilu/
├── frontend/             # Next.js PWA
│   ├── package.json
│   └── app/
├── docs/                 # Documentação
├── docker-compose.yml    # PostgreSQL + Adminer
├── package.json          # Root scripts
└── README.md
```

##  Scripts Disponíveis

```bash
# Desenvolvimento (Frontend + Backend)
npm run dev

# Apenas Backend
npm run dev:backend

# Apenas Frontend
npm run dev:frontend

# Build tudo
npm run build

# Build Backend
npm run build:backend

# Build Frontend
npm run build:frontend
```

##  Autenticação

A API usa JWT com dois roles:
- **ADMIN**: Pode criar filas e chamar próximo
- **CLIENTE**: Pode entrar em fila e ver posição

Endpoint de login:
```bash
POST /api/auth/login
{
  "usuarioId": "550e8400-e29b-41d4-a716-446655440000",
  "role": "CLIENTE"
}
```

##  Endpoints Principais

| Método | Endpoint | Permissão |
|--------|----------|-----------|
| POST | `/filas` | ADMIN |
| GET | `/filas/{id}` | Público |
| POST | `/filas/{id}/entrar` | CLIENTE |
| POST | `/filas/{id}/chamar-proximo` | ADMIN |
| GET | `/filas/{id}/posicao/{usuarioId}` | CLIENTE |

## 🌐 Variáveis de Ambiente

### Frontend (`.env.local`)
```bash
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

### Backend (`application.yml`)
```bash
DATABASE_URL=r2dbc:postgresql://localhost:5432/kuilu_db
DATABASE_USER=postgres
DATABASE_PASSWORD=postgres
JWT_SECRET=seu-secret-aqui
JWT_EXPIRATION=86400000
```

##  Testes

### Backend
```bash
cd backend
mvn test
```

### Frontend
```bash
cd frontend
npm test
```

##  Documentação

- [Backend README](./backend/README.md)
- [Frontend README](./frontend/README.md)

##  Docker

### Iniciar PostgreSQL
```bash
docker-compose up -d
```

### Parar
```bash
docker-compose down
```

### Ver logs
```bash
docker-compose logs -f postgres
```

##  Roadmap

- [ ] Sistema de notificações em tempo real (WebSocket)
- [ ] Mobile app nativa (React Native)
- [ ] Analytics e relatórios
- [ ] Integração com pagamento
- [ ] Multi-idioma
- [ ] Autenticação com SMS

## Contribuições

Contribuições são bem-vindas! Por favor:

1. Fork o projeto
2. Crie sua feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 📞 Contato

- **GitHub**: [Helio-206](https://github.com/Helio-206)
- **Projeto**: [https://github.com/Helio-206/Kuilu](https://github.com/Helio-206/Kuilu)

---

**Kuilu - Sua vez, sem perder tempo** 
