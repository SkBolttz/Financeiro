# FinTrack

**FinTrack** é uma aplicação completa de controle financeiro pessoal, que permite gerenciar **receitas, despesas, limites, fornecedores e clientes**, com **autenticação JWT** e **dashboard interativo**.  
O back-end é em **Java + Spring Boot** e o front-end em **React + Vite**.

> Resumo rápido: API REST segura (JWT) para gerenciar movimentações financeiras, categorias, clientes e fornecedores, com endpoints para relatórios e KPIs usados pelo dashboard.

---

## Principais funcionalidades
- Cadastro e autenticação de usuários (JWT)
- CRUD de **Categorias**, **Clientes**, **Fornecedores**
- Gerenciamento completo de **Movimentações** (Receitas e Despesas):
  - Adicionar (com upload de comprovantes)
  - Editar
  - Remover
  - Marcar como paga
  - Listar com paginação
  - Filtrar por status (ativa/inativa, paga/atrasada)
- **Alertas** de vencimento e ajuste automático de vencimentos de despesas
- **Dashboard** com KPIs e relatórios:
  - Receitas x despesas
  - Top clientes e fornecedores
  - Despesas por categoria
  - Extratos detalhados
- Controle de **limites** por cliente

---

## Stack / Tecnologias

**Backend**
- Java 21  
- Spring Boot  
- Spring Security (JWT)  
- Spring Data JPA  
- PostgreSQL  
- Swagger / OpenAPI  

**Frontend**
- React  
- Vite  
- HTML, CSS, JavaScript  

**Infra**
- Frontend hospedado em Vercel: [https://fintrack-finance.vercel.app](https://fintrack-finance.vercel.app)

---

## Endpoints da API

A API segue padrão REST. A maioria dos endpoints exige autenticação via JWT (`Authorization: Bearer <token>`).

### Alertas (`/alerta`)
- `GET /alerta/verificar/vencimento/amanha`  
  Lista movimentações que vencem amanhã para o usuário logado
- `GET /alerta/alterar/vencimento/despesa`  
  Ajusta vencimento de despesas vencidas e retorna alertas das movimentações alteradas

### Categoria (`/categoria`)
- `POST /categoria/cadastrar` — Cria nova categoria  
- `PUT /categoria/remover` — Remove categoria  
- `PUT /categoria/editar` — Atualiza categoria existente  
- `GET /categoria/listar` — Lista todas as categorias do usuário  
- `GET /categoria/listar/ativas` — Lista categorias ativas  
- `GET /categoria/listar/inativas` — Lista categorias inativas  
- `GET /categoria/listar/receita/ativas` — Lista categorias de receita ativas  
- `GET /categoria/listar/despesa/ativas` — Lista categorias de despesa ativas  

### Cliente (`/clientes`)
- `GET /clientes/listar` — Lista todos os clientes do usuário  
- `GET /clientes/listar/ativos` — Lista clientes ativos  
- `GET /clientes/buscar/{id}` — Busca cliente por ID  
- `POST /clientes/adicionar` — Cria novo cliente  
- `PUT /clientes/editar/{id}` — Atualiza cliente existente  
- `PUT /clientes/desativar/{id}` — Marca cliente como inativo  
- `PUT /clientes/ativar/{id}` — Marca cliente como ativo  

### Fornecedores (`/fornecedores`)
- `POST /fornecedores/adicionar` — Cria novo fornecedor  
- `PUT /fornecedores/editar/{id}` — Atualiza fornecedor  
- `PUT /fornecedores/desativar/{id}` — Marca fornecedor como inativo  
- `PUT /fornecedores/ativar/{id}` — Marca fornecedor como ativo  
- `GET /fornecedores/listar` — Lista todos os fornecedores  
- `GET /fornecedores/listar/ativos` — Lista fornecedores ativos  
- `GET /fornecedores/buscar/{id}` — Busca fornecedor por ID  

### Limite (`/limite`)
- `POST /limite/cadastrar` — Cria ou atualiza limite por cliente  
- `GET /limite/listar/limite/cliente` — Retorna limite do usuário logado  
- `GET /limite/verificar/limite` — Verifica limite atual do usuário  

### Movimentação (`/movimentacao`)
- `POST /movimentacao/adicionar/receita` — Adiciona receita com comprovantes  
- `POST /movimentacao/adicionar/despesa` — Adiciona despesa com comprovantes  
- `PUT /movimentacao/remover/receita` — Remove receita  
- `PUT /movimentacao/remover/despesa` — Remove despesa  
- `PUT /movimentacao/editar/receita` — Edita receita  
- `PUT /movimentacao/editar/despesa` — Edita despesa  
- `PUT /movimentacao/alterar/movimentacao/paga` — Marca movimentação como paga  
- Listagens com paginação (`page` e `size` query params) para:
  - Todas movimentações, receitas, despesas  
  - Movimentações ativas/inativas  
  - Despesas pagas/atrasadas  
  - Movimentações recorrentes  
- `GET /movimentacao/listar/movimentacao/{idMovimentacao}` — Detalhes completos de uma movimentação  

---

## Autenticação
- JWT (Bearer Token) para proteger endpoints  
- Header de autorização:
```http
Authorization: Bearer <token>

## Estrutura dos DTOs / Entidades (visão geral)

### Movimentações
- `MovimentacaoDTO`  
- `AlterarMovimentacaoDTO`  
- `RemoverMovimentacaoDTO`  
- `ListarMovimentacaoDTO`  

### Categorias
- `Categoria`  
- `CategoriaDTO`  

### Clientes
- `ClienteDTO`  
- `AlterarClienteDTO`  

### Fornecedores
- `FornecedorDTO`  
- `AlterarFornecedorDTO`  

### Limite
- `Limite`  

### Alertas
- `AlertaDTO`  

### Dashboard
- `ExtratoMovimentacaoDTO`  
- `DespesaCategoriaDTO`  
- `MovimentacaoCategoriaDTO`  
- `MovimentacaoMensalDTO`  
- `MovimentacaoRecorrenteDTO`  
- `MovimentacaoTipoPagamentoDTO`  
- `ClientesPorMesDTO`  
- `ClientesTipoDTO`  
- `TopClientesDTO`  
- `TopFornecedoresDTO`  
- `TotalReceitaDespesaDTO`  
- `FinanceiroKpiDTO`  
