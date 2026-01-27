# Kuilu Frontend

Interface web mobile-first para gestão de filas digitais em Angola.

## 🚀 Stack

- **Next.js** 14+ com App Router
- **TypeScript** para type safety
- **Tailwind CSS** para estilo
- **PWA** para offline e mobile
- **Responsive** mobile-first design

## 📋 Requisitos

- Node.js 18+
- npm ou yarn

## 🔧 Setup

```bash
# Instalar dependências
npm install

# Criar .env.local
cp .env.example .env.local

# Desenvolvimento
npm run dev

# Build
npm run build

# Produção
npm start
```

## 📚 Estrutura

```
app/
├── components/         # Componentes reutilizáveis
├── services/          # API client
├── types/             # TypeScript interfaces
├── fila/[id]/        # Página de fila
├── criar-fila/       # Criar fila (admin)
├── page.tsx          # Home
├── layout.tsx        # Layout raiz
└── globals.css       # Estilos globais
```

## 🎨 Componentes

- **Button**: Botão grande e acessível
- **Card**: Container para conteúdo
- **Header**: Cabeçalho com navegação
- **Loader**: Spinner de carregamento

## 📱 Páginas

| Rota | Descrição |
|------|-----------|
| `/` | Home - Entrar na fila |
| `/fila/{id}` | Visualizar posição |
| `/criar-fila` | Criar fila (admin) |

## 🔌 API

Endpoints conectados ao backend:

- `POST /auth/login` - Autenticação
- `POST /filas` - Criar fila
- `GET /filas/{id}` - Detalhes
- `POST /filas/{id}/entrar` - Entrar
- `GET /filas/{id}/posicao/{usuarioId}` - Posição

## 🌐 PWA

O app é configurado como PWA:

```bash
# Instalar como app
- iOS: Share > Add to Home Screen
- Android: Menu > Install app
```

## 🎯 Features

✅ Mobile-first design  
✅ Grande tipografia e botões  
✅ Polling de 5s para atualização  
✅ Baixo consumo de dados  
✅ Suporte offline (manifest)  
✅ Suporte a múltiplas línguas (pt-AO)  
✅ Acessibilidade (ARIA labels)  

## 📖 Variáveis de Ambiente

```bash
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

## 🧪 Testes

```bash
npm test
```

## 📝 Notas

- Usuário é armazenado em localStorage
- Token é simulado para demo
- Polling a cada 5 segundos para atualizações
- Design mobile-first com max-width 480px
