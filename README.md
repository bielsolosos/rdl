# Redirect Lab (RDL)

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)


## Visão Geral

O RDL foi desenvolvido com foco em **modularidade** e **baixo acoplamento**, permitindo que módulos inteiros (especialmente o de `Users`) possam ser reutilizados em outros projetos com mínimas alterações.

### Status do Projeto

| Componente | Status |
|------------|--------|
| Core Infrastructure | ✅ Estável |
| Autenticação Web (Session + CSRF) | ✅ Estável |
| Autenticação API (JWT + Refresh Token) | ✅ Estável |
| Módulo de URLs | ✅ Estável |
| Módulo de Usuários | 🔄 Em desenvolvimento |
| Dashboard & Métricas | 📋 Planejado |

---

## Stack Tecnológica

### Backend

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Java** | 21 | Linguagem principal com features modernas (Records, Pattern Matching) |
| **Spring Boot** | 3.5.8 | Framework base com auto-configuração |
| **Spring Security** | 6.x | Autenticação e autorização |
| **Spring Data JPA** | - | ORM e persistência |
| **Flyway** | - | Migrations e versionamento de banco |
| **Lombok** | - | Redução de boilerplate |
| **JJWT** | 0.12.5 | Geração e validação de JWT |

### Frontend (Server-Side Rendering)

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Thymeleaf** | 3.x | Template engine server-side |
| **Thymeleaf Extras Spring Security 6** | - | Integração sec:authorize |
| **DaisyUI** | 4.12.14 | Componentes UI (tema Winter) |
| **Tailwind CSS** | CDN | Utility-first CSS |
| **HTMX** | 2.0.4 | Interatividade sem JavaScript complexo |
| **Lucide Icons** | latest | Ícones SVG |

### Banco de Dados & Infraestrutura

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **PostgreSQL** | 16 | Banco de dados relacional |
| **Flyway** | - | Controle de versão do schema |
| **Spring Dotenv** | 4.0.0 | Gerenciamento de variáveis .env |

---

## Arquitetura

O projeto segue uma arquitetura em camadas com **baixo acoplamento** entre módulos, inspirada em Clean Architecture e Domain-Driven Design (DDD) simplificado.

```
┌─────────────────────────────────────────────────────────────┐
│                      API Layer                              │
│    Controllers (REST + Web), DTOs, Mappers                  │
│    Comunicação com o mundo externo                          │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                    Domain Layer                             │
│    Entities, Repositories, Services                         │
│    Regras de negócio, tokens, validações                    │
│    ⭐ Módulo reutilizável em outros projetos                │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                     Core Layer                              │
│    SecurityConfig, Filters, Exception Handlers              │
│    Configurações globais e respostas padrão                 │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                Infrastructure Layer                         │
│    Properties, Schedulers, Integrações externas             │
└─────────────────────────────────────────────────────────────┘
```

### Estrutura de Diretórios

```
src/main/java/space/bielsolososdev/rdl/
├── api/                              # Camada de API
│   ├── controller/
│   │   ├── rest/                     # Controllers REST (JWT Auth)
│   │   └── web/                      # Controllers Web (Session Auth)
│   ├── mapper/                       # Conversores DTO <-> Entity
│   └── model/                        # DTOs e Records
│
├── core/                             # Camada Core (Configurações)
│   ├── config/                       # Configurações gerais
│   ├── exception/                    # Exceções customizadas
│   │   └── globalconfig/             # GlobalExceptionHandler
│   ├── security/                     # Spring Security Config
│   │   ├── SecurityConfig.java       # Configuração de rotas e filtros
│   │   └── JwtAuthenticationFilter.java
│   └── utils/                        # Utilitários
│
├── domain/                           # Camada de Domínio (Regras de Negócio)
│   ├── url/                          # Módulo de URLs
│   │   ├── model/                    # Entity UrlRedirect
│   │   ├── repository/               # JPA Repository
│   │   └── service/                  # UrlRedirectService
│   │
│   └── users/                        # Módulo de Usuários ⭐
│       ├── model/                    # Entity User, Role
│       ├── repository/               # UserRepository
│       └── service/
│           ├── UserService.java      # CRUD de usuários
│           ├── AuthService.java      # Login JWT + Refresh
│           ├── RefreshTokenService.java
│           └── CustomUserDetailsService.java
│
├── infrastructure/                   # Camada de Infraestrutura
│   ├── RdlProperties.java            # @ConfigurationProperties
│   └── scheduler/                    # Jobs agendados
│
└── RdlApplication.java               # Entry point
```

### Filosofia de Modularidade

A **camada Domain** foi projetada para ser **copiável** para outros projetos:

- **Baixo acoplamento**: Dependências mínimas com outras camadas
- **Self-contained**: Cada módulo possui Entity, Repository e Service
- **Exceção controlada**: JwtService na Domain depende de Core (RdlProperties) e Infrastructure, mas essa é uma dependência aceitável e documentada

> 💡 **Dica**: Para reutilizar o módulo `users` em outro projeto, basta copiar a pasta `domain/users`, ajustar o package e configurar as properties de JWT.

---

## Sistema de Autenticação

O RDL implementa **dois sistemas de autenticação** para diferentes casos de uso:

### 1. Autenticação Web (Session + CSRF)

Para as páginas renderizadas com Thymeleaf:

- **Spring Security Form Login** com sessões HTTP
- **CSRF Protection** integrado com HTMX
- **Remember-me** com cookie persistente
- **Thymeleaf Security Dialect** (`sec:authorize`, `sec:authentication`)

```html
<!-- Exemplo de uso no Thymeleaf -->
<div sec:authorize="isAuthenticated()">
    <span sec:authentication="name">Usuário</span>
</div>
```

### 2. Autenticação API (JWT + Refresh Token)

Para integrações REST:

- **Access Token JWT** (curta duração)
- **Refresh Token** (longa duração, single-use)
- **Stateless** para APIs
- **Filtro JWT** apenas para rotas `/api/**`

```bash
# Login
POST /api/auth/login
{
  "username": "user",
  "password": "password"
}

# Response
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "uuid-token",
  "expiresIn": 900000
}

# Refresh
POST /api/auth/refresh
{
  "refreshToken": "uuid-token"
}
```

### Rotas e Permissões

| Rota | Método | Autenticação | Descrição |
|------|--------|--------------|-----------|
| `/` | GET | Pública | Home page |
| `/login` | GET/POST | Pública | Login web |
| `/urls/**` | * | Session | Gerenciamento URLs (web) |
| `/profile/**` | * | Session | Perfil do usuário |
| `/api/auth/**` | POST | Pública | Endpoints de autenticação |
| `/api/**` | * | JWT | API REST protegida |
| `/redirect/{slug}` | GET | Pública | Redirecionamento |

---

## Funcionalidades Atuais

### Interface Web (Thymeleaf + HTMX)

- ✅ Login/Logout com sessão
- ✅ Gerenciamento completo de URLs (CRUD)
- ✅ Toggle de status com HTMX (sem reload)
- ✅ Copiar link para clipboard
- ✅ Página de perfil com troca de senha
- ✅ Design responsivo (DaisyUI Winter theme)
- ✅ Ícones SVG (Lucide Icons)

### API REST

- ✅ Autenticação JWT com refresh token
- ✅ CRUD de redirects protegido
- ✅ Tratamento global de exceções
- ✅ Respostas padronizadas (MessageResponse)

### Sistema

- ✅ Migrations automáticas (Flyway)
- ✅ Cleanup automático de refresh tokens expirados
- ✅ Health check endpoint
- ✅ Página 404 customizada

---

## Roadmap

### O que realmente falta fazer

- [ ] **Detecção de bots** com logging específico
- [ ] **Honey pots** para identificar tentativas maliciosas
- [ ] **Gerenciamento de roles** (ADMIN, USER)
- [ ] **Recuperação de senha** por email
- [ ] **Cache via memória RAM** Redis/Caffeine
- [ ] **Docker-Compose criando NetWork**

### Redirects por Usuário

- [ ] **Ownership de URLs**: cada usuário gerencia seus próprios redirects
- [ ] **URLs públicas vs privadas**
- [ ] **Limite de URLs** por plano/role
- [ ] **Slugs personalizados** por usuário


### Features Avançadas

- [ ] **QR Code** gerado automaticamente
- [ ] **Links com expiração** (TTL configurável)
- [ ] **Preview de destino** antes de redirecionar
- [ ] **Tags e categorias** para organização
- [ ] **API pública** com rate limiting

---

## Instalação

### Pré-requisitos

- Java 21+
- PostgreSQL 16+
- Maven 3.8+

### Setup

```bash
# Clone o repositório
git clone https://github.com/bielsolosos/rdl.git
cd rdl

# Crie o banco de dados
psql -U postgres -c "CREATE DATABASE \"rdl-db\";"

# Configure as variáveis de ambiente
cp .env.example .env
# Edite o .env com suas configurações

# Execute
./mvnw spring-boot:run
```

### Variáveis de Ambiente

```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/rdl-db
DB_USERNAME=postgres
DB_PASSWORD=sua_senha

# JWT
JWT_SECRET=sua-chave-secreta-256-bits-minimo
JWT_EXPIRATION=900000
JWT_REFRESH_EXPIRATION=604800000

# App
APP_NAME=Redirect Lab
SHOW_SQL=false
```

---

## API Reference

### Autenticação

```bash
# Login
POST /api/auth/login
Content-Type: application/json
{
  "username": "admin",
  "password": "password"
}

# Refresh Token
POST /api/auth/refresh
Content-Type: application/json
{
  "refreshToken": "uuid-refresh-token"
}
```

### Redirects (Requer JWT)

```bash
# Listar
GET /api/redirect
Authorization: Bearer {accessToken}

# Criar
POST /api/redirect
Authorization: Bearer {accessToken}
{
  "slug": "github",
  "url": "https://github.com/bielsolosos",
  "isEnabled": true
}

# Toggle status
PATCH /api/redirect/{id}/toggle
Authorization: Bearer {accessToken}

# Deletar
DELETE /api/redirect/{id}
Authorization: Bearer {accessToken}
```


## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

<div align="center">

**Desenvolvido por [@bielsolosos](https://github.com/bielsolosos)**

⭐ Se este projeto foi útil, considere dar uma estrela!

</div>
