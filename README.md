# 🔗 RDL - Redirect Lab

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

> **RDL (Redirect Lab)** é um encurtador de URLs moderno desenvolvido com Spring Boot 3 e Thymeleaf, focado em simplicidade, performance e uma interface elegante com DaisyUI. Aplicação full-stack pronta para uso direto e integração com APIs externas.

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Tecnologias](#-tecnologias)
- [Arquitetura](#-arquitetura)
- [Funcionalidades](#-funcionalidades)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação](#-instalação)
- [Configuração](#-configuração)
- [API REST](#-api-rest)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Banco de Dados](#-banco-de-dados)
- [Contribuindo](#-contribuindo)

---

## 🎯 Sobre o Projeto

O **RDL** é um sistema completo de encurtamento de URLs desenvolvido com **Spring Boot 3 + Thymeleaf**, oferecendo uma aplicação web full-stack pronta para uso. Com interface moderna construída em DaisyUI e HTMX, o sistema permite gerenciar links encurtados de forma intuitiva e responsiva.

### ✨ Principais Características

- 🚀 **Performance**: Spring Boot 3 com Java 21
- 🎨 **Interface Moderna**: Thymeleaf + DaisyUI + Tailwind CSS (tema dark)
- 🔄 **Interatividade**: HTMX para atualizações dinâmicas sem reload
- 🔒 **Seguro**: Validações robustas e tratamento de exceções global
- 📊 **Migrations**: Flyway para controle de versão do banco
- 🐘 **PostgreSQL**: Banco de dados relacional confiável
- 🔄 **CRUD Completo**: Interface web com Create, Read, Update, Delete e Toggle
- 🌐 **REST API**: Endpoints prontos para integração externa
- 📱 **Responsivo**: Layout adaptável para mobile, tablet e desktop

---

## 🛠️ Tecnologias

### Backend
- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.8** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Validation** - Validação de dados
- **Lombok** - Redução de boilerplate

### Frontend
- **Thymeleaf** - Template engine server-side
- **DaisyUI 4.12.14** - Biblioteca de componentes UI
- **Tailwind CSS** - Framework CSS utility-first
- **HTMX 2.0.4** - Interatividade sem JavaScript complexo

### Banco de Dados
- **PostgreSQL** - Banco de dados relacional
- **Flyway** - Migrations e controle de versão

### Ferramentas
- **Maven** - Gerenciamento de dependências
- **Spring DevTools** - Hot reload em desenvolvimento
- **Spring Dotenv** - Gerenciamento de variáveis de ambiente

---

## 🏗️ Arquitetura

O projeto segue uma **arquitetura em camadas** (Layered Architecture) com separação clara de responsabilidades:

```
┌─────────────────────────────────────────┐
│           API Layer                     │
│  (Controllers, DTOs, Mappers)          │
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│         Domain Layer                    │
│  (Services, Entities, Repositories)    │
└─────────────┬───────────────────────────┘
              │
┌─────────────▼───────────────────────────┐
│          Core Layer                     │
│  (Exceptions, Configurations)          │
└─────────────────────────────────────────┘
```

---

## ⚡ Funcionalidades

### ✅ Implementadas

#### Interface Web (Thymeleaf + HTMX)
- [x] **Gerenciamento Completo de URLs** - Interface web full-stack para criar, listar, editar e deletar redirects
- [x] **Atualizações Dinâmicas** - HTMX para interações sem reload de página
- [x] **Design Responsivo** - Layout adaptável para mobile, tablet e desktop
- [x] **Copiar para Clipboard** - Botão para copiar links encurtados
- [x] **Toggle de Status** - Habilitar/desabilitar redirects com um clique
- [x] **Confirmação de Exclusão** - Dialog de confirmação antes de deletar

#### API REST
- [x] **Criar Redirect** - Endpoint POST para criar novos links encurtados
- [x] **Listar Redirects** - Endpoint GET para listar todos os redirects
- [x] **Buscar por Slug** - Buscar redirect específico
- [x] **Atualizar Redirect** - Endpoint PUT para atualização completa
- [x] **Deletar Redirect** - Endpoint DELETE para remoção
- [x] **Habilitar/Desabilitar** - Endpoint PATCH para toggle de status
- [x] **Redirecionamento** - Sistema de redirecionamento para URL original

#### Sistema
- [x] **Página 404 Customizada** - Interface elegante para erros
- [x] **Health Check** - Monitoramento do status da aplicação
- [x] **Tratamento Global de Exceções** - Handler centralizado com MessageResponse
- [x] **Validações Robustas** - Validação de dados em todas as camadas

### 🔜 Roadmap

#### 🔐 Autenticação e Autorização (Fase 1)
- [ ] Sistema de registro de usuários
- [ ] Login com email e senha
- [ ] Sessões com Spring Security
- [ ] Páginas protegidas (dashboard, gerenciamento de URLs)
- [ ] Rotas públicas vs. rotas autenticadas
- [ ] Gerenciamento de permissões (usuário comum vs. admin)

#### 🎫 API com Autenticação JWT (Fase 2)
- [ ] Implementação de JWT (JSON Web Token)
- [ ] Endpoints de autenticação (`/api/auth/login`, `/api/auth/register`)
- [ ] Refresh tokens para renovação de sessão
- [ ] Middleware de autenticação JWT
- [ ] Proteção de endpoints REST com tokens
- [ ] Documentação Swagger/OpenAPI com autenticação

#### 📊 Funcionalidades Avançadas (Fase 3)
- [ ] Dashboard de usuário com estatísticas pessoais
- [ ] Estatísticas de cliques por redirect
- [ ] Histórico de acessos (IP, localização, navegador)
- [ ] QR Code gerado automaticamente para cada link
- [ ] Sistema de expiração de links (TTL configurável)
- [ ] Personalização avançada de slugs
- [ ] Tags e categorias para organização de links
- [ ] Exportação de dados (CSV, JSON)

#### 🎨 Melhorias de Interface (Fase 4)
- [ ] Área de perfil do usuário
- [ ] Temas customizáveis (light/dark/auto)
- [ ] Gráficos interativos de estatísticas
- [ ] Preview de URL antes de redirecionar
- [ ] Notificações em tempo real

---

## 📦 Pré-requisitos

- **Java 21** ou superior
- **PostgreSQL 16** ou superior
- **Maven 3.8+**
- **Git**

---

## 🚀 Instalação

### 1. Clone o repositório

```bash
git clone https://github.com/bielsolosos/rdl.git
cd rdl
```

### 2. Configure o banco de dados

Crie o banco de dados PostgreSQL:

```sql
CREATE DATABASE "rdl-db";
```

### 3. Configure as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto (use `.env.example` como base):

```env
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/rdl-db
DB_USERNAME=postgres
DB_PASSWORD=sua_senha

# JPA Configuration
SHOW_SQL=false
```

### 4. Execute o projeto

```bash
# Com Maven Wrapper
./mvnw spring-boot:run

# Ou com Maven instalado
mvn spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

---

## ⚙️ Configuração

### application.yml

```yaml
spring:
  application:
    name: rdl
  
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/rdl-db}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: ${SHOW_SQL:false}
  
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
```

---

## 🌐 API REST

### Base URL
```
http://localhost:8080
```

### Endpoints

#### 🔗 Redirects

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/redirect/{slug}` | Redireciona para URL original |
| `GET` | `/redirect` | Lista todos os redirects |
| `POST` | `/redirect` | Cria novo redirect |
| `PUT` | `/redirect/{id}` | Atualiza redirect |
| `DELETE` | `/redirect/{id}` | Deleta redirect |
| `PATCH` | `/redirect/{id}/toggle` | Habilita/Desabilita redirect |

#### 📄 Páginas Web

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/` | Página inicial |
| `GET` | `/urls` | Gerenciamento de URLs (lista, criar, editar, deletar) |
| `POST` | `/urls` | Criar novo redirect (HTMX) |
| `POST` | `/urls/{id}/toggle` | Habilitar/desabilitar redirect (HTMX) |
| `POST` | `/urls/{id}/delete` | Deletar redirect (HTMX) |
| `GET` | `/health` | Status da aplicação |
| `GET` | `/error/404` | Página de erro 404 |

### Exemplos de Requisições

#### Criar Redirect

```bash
POST /redirect
Content-Type: application/json

{
  "slug": "github",
  "url": "https://github.com/bielsolosos",
  "isEnabled": true
}
```

**Resposta:**
```json
{
  "id": 1,
  "slug": "github",
  "url": "https://github.com/bielsolosos",
  "isEnabled": true
}
```

#### Atualizar Redirect

```bash
PUT /redirect/1
Content-Type: application/json

{
  "slug": "github-new",
  "url": "https://github.com/bielsolosos/rdl",
  "isEnabled": true
}
```

#### Habilitar/Desabilitar

```bash
PATCH /redirect/1/toggle
```

#### Deletar Redirect

```bash
DELETE /redirect/1
```

**Resposta:**
```json
{
  "message": "Redirect deletado com sucesso"
}
```

### Tratamento de Erros

Todas as respostas de erro seguem o padrão:

```json
{
  "message": "Descrição do erro"
}
```

**Códigos HTTP:**
- `200` - Sucesso
- `400` - Erro de validação/negócio
- `404` - Recurso não encontrado
- `500` - Erro interno do servidor

---

## 📁 Estrutura do Projeto

```
rdl/
├── src/
│   ├── main/
│   │   ├── java/space/bielsolososdev/rdl/
│   │   │   ├── api/                          # Camada de API
│   │   │   │   ├── controller/
│   │   │   │   │   ├── rest/                 # Controllers REST
│   │   │   │   │   │   ├── HomeRestController.java
│   │   │   │   │   │   └── UrlRedirectController.java
│   │   │   │   │   └── web/                  # Controllers Web (Thymeleaf)
│   │   │   │   │       ├── ErrorController.java
│   │   │   │   │       ├── HomeController.java
│   │   │   │   │       └── UrlManagementController.java
│   │   │   │   ├── mapper/                   # Mappers DTO <-> Entity
│   │   │   │   │   └── UrlRedirectMapper.java
│   │   │   │   └── model/                    # DTOs/Records
│   │   │   │       ├── HealthStatusResponse.java
│   │   │   │       ├── MessageResponse.java
│   │   │   │       └── urlredirect/
│   │   │   │           ├── UrlRedirectRequest.java
│   │   │   │           └── UrlRedirectResponse.java
│   │   │   │
│   │   │   ├── core/                         # Camada Core
│   │   │   │   └── exception/
│   │   │   │       ├── BusinessException.java
│   │   │   │       ├── RedirectException.java
│   │   │   │       └── globalconfig/
│   │   │   │           └── GlobalExceptionHandler.java
│   │   │   │
│   │   │   ├── domain/                       # Camada de Domínio
│   │   │   │   └── url/
│   │   │   │       ├── model/
│   │   │   │       │   └── UrlRedirect.java  # Entity
│   │   │   │       ├── repository/
│   │   │   │       │   └── UrlRedirectRepository.java
│   │   │   │       └── service/
│   │   │   │           └── UrlRedirectService.java
│   │   │   │
│   │   │   └── RdlApplication.java           # Main class
│   │   │
│   │   └── resources/
│   │       ├── db/migration/                 # Flyway migrations
│   │       │   └── V1__create_url_redirect_table.sql
│   │       ├── templates/                    # Thymeleaf templates
│   │       │   ├── layout/
│   │       │   │   └── base.html             # Layout base com navbar
│   │       │   ├── error/
│   │       │   │   └── 404.html              # Página 404 customizada
│   │       │   ├── urls/
│   │       │   │   └── list.html             # Gerenciamento de URLs (HTMX)
│   │       │   ├── index.html                # Página inicial
│   │       │   └── health.html               # Health check
│   │       └── application.yml
│   │
│   └── test/                                 # Testes
│
├── .env.example                              # Exemplo de variáveis
├── pom.xml                                   # Maven dependencies
└── README.md
```

### Camadas do Projeto

#### 🌐 API Layer
Responsável pela comunicação com o mundo externo (REST APIs e páginas web).

- **Controllers REST**: Endpoints da API
- **Controllers Web**: Páginas HTML com Thymeleaf
- **DTOs**: Objetos de transferência de dados
- **Mappers**: Conversão entre DTOs e Entities

#### 💼 Domain Layer
Contém as regras de negócio e lógica da aplicação.

- **Entities**: Modelos do banco de dados (JPA)
- **Repositories**: Interface com o banco (Spring Data)
- **Services**: Lógica de negócio

#### ⚙️ Core Layer
Configurações e funcionalidades transversais.

- **Exceptions**: Exceções customizadas
- **Global Handlers**: Tratamento centralizado de erros

---

## 🗄️ Banco de Dados

### Modelo de Dados

#### Tabela: `urls_redirect`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | BIGSERIAL | ID único (chave primária) |
| `slug` | VARCHAR(50) | Código curto da URL (único) |
| `url` | VARCHAR(255) | URL original completa |
| `is_enabled` | BOOLEAN | Status de ativação |
| `created_at` | TIMESTAMP | Data de criação |
| `updated_at` | TIMESTAMP | Data de atualização |

**Índices:**
- `idx_slug` - Índice único no campo slug (busca rápida)
- `idx_is_enabled` - Índice no campo is_enabled (filtros)

### Migrations

O projeto utiliza **Flyway** para versionamento do banco de dados.

Localização: `src/main/resources/db/migration/`

#### V1__create_url_redirect_table.sql
```sql
CREATE TABLE urls_redirect (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(50) UNIQUE NOT NULL,
    url VARCHAR(255) NOT NULL,
    is_enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_slug ON urls_redirect(slug);
CREATE INDEX idx_is_enabled ON urls_redirect(is_enabled);
```

---

## 🎨 Interface

### Páginas Disponíveis

#### 🏠 Home (`/`)
Página inicial com informações do sistema e links de navegação.

#### 🔗 Gerenciamento de URLs (`/urls`)
Interface completa para gerenciar redirects com:
- Formulário de criação de novos links
- Tabela com listagem de todos os redirects
- Botões de ação (copiar, habilitar/desabilitar, deletar)
- Atualizações dinâmicas via HTMX (sem reload)
- Design responsivo adaptável para mobile
- Confirmação antes de deletar

#### 🏥 Health Check (`/health`)
Dashboard com informações detalhadas:
- Status da aplicação
- Conexão com banco de dados
- Configurações do sistema
- Uptime e performance

#### ❌ Erro 404 (`/error/404`)
Página customizada para URLs não encontradas, com:
- Design elegante com DaisyUI
- Informações sobre o erro
- Slug solicitado (quando disponível)
- Botões de navegação

### Tema e Experiência

O projeto utiliza o tema **Dark** do DaisyUI, proporcionando:
- ✅ Interface moderna e profissional
- ✅ Melhor experiência visual em ambientes com pouca luz
- ✅ Componentes responsivos para todos os dispositivos
- ✅ Animações suaves e transições elegantes
- ✅ Interatividade via HTMX sem complexidade do JavaScript moderno

---

## 🔒 Segurança e Validações

### Validações Implementadas

- ✅ Slug único no sistema
- ✅ URL única no sistema
- ✅ Validação de campos obrigatórios
- ✅ Verificação de existência antes de atualizar/deletar
- ✅ Validação de tamanhos máximos (slug: 50, url: 255)

### Tratamento de Exceções

O **GlobalExceptionHandler** captura e trata automaticamente:

1. **BusinessException** → Retorna JSON com mensagem de erro (HTTP 400)
2. **RedirectException** → Redireciona para página 404 customizada
3. **Exception genérica** → Retorna mensagem de erro interno (HTTP 500)

---

## 🚦 Como Usar

### 1. Acessar a Aplicação
```
http://localhost:8080
```

### 2. Interface Web - Gerenciar URLs
Acesse `/urls` para:
- Criar novos redirects preenchendo o formulário
- Visualizar todos os links cadastrados
- Copiar links encurtados para clipboard
- Habilitar/desabilitar redirects existentes
- Deletar redirects (com confirmação)

### 3. API REST - Criar um Redirect
Integre com sistemas externos via API:

```bash
curl -X POST http://localhost:8080/redirect \
  -H "Content-Type: application/json" \
  -d '{
    "slug": "meu-link",
    "url": "https://exemplo.com/pagina-muito-longa",
    "isEnabled": true
  }'
```

### 4. Acessar o Link Curto
```
http://localhost:8080/redirect/meu-link
```

O usuário será automaticamente redirecionado para a URL original!

### 5. Integração Externa
A API REST está pronta para ser consumida por:
- Aplicações frontend (React, Vue, Angular)
- Apps mobile (Android, iOS)
- Sistemas backend de terceiros
- Scripts e automações

**Exemplo de integração JavaScript:**
```javascript
// Criar redirect
const response = await fetch('http://localhost:8080/redirect', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    slug: 'github',
    url: 'https://github.com/usuario',
    isEnabled: true
  })
});

const redirect = await response.json();
console.log(`Link criado: ${window.location.origin}/redirect/${redirect.slug}`);
```

---

<div align="center">

**⭐ Se este projeto foi útil, considere dar uma estrela no GitHub!**

</div>
