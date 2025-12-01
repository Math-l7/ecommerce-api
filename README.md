# 🛒 E-commerce API

API RESTful desenvolvida em **Spring Boot** para gerenciar usuários, produtos e pedidos — estruturada com foco em **boas práticas DevOps**, **segurança**, **padronização profissional** e **deploy moderno usando Docker e Docker Compose**.

---

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3**
- **Spring Security + JWT**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **JUnit 5 + Mockito**
- **Swagger/OpenAPI 3**
- **Docker & Docker Compose**

---

## 🧱 Arquitetura da Aplicação

A arquitetura foi planejada seguindo práticas utilizadas em aplicações profissionais, com separação clara de camadas, segurança, isolamentos e ambiente totalmente containerizado.

---

## 🧾 Integração com Payment Gateway (Simulação Realista)

A aplicação conta com um **módulo interno de Payment Gateway**, responsável por processar pagamentos de forma desacoplada e segura.  
Embora utilize um endpoint externo para simulação (`https://httpbin.org/post`), o fluxo foi projetado seguindo padrões usados em gateways reais como **Stripe**, **Mercado Pago**, **PagSeguro** e **PayPal**.

---

### 🔌 Como funciona o fluxo de pagamento

1. O service `PaymentGateWayService` recebe os dados do pedido.
2. Monta um payload contendo:
   - `orderId`
   - `amount`
   - `customerEmail`
3. Envia os dados para o gateway via `RestTemplate`.
4. Valida:
   - Status HTTP
   - Integridade da resposta
5. Se **sucesso** → o pedido é marcado como **pago**.
6. Se **erro** → o pedido permanece **pendente**, simulando um processamento real.

---

## 📂 Estrutura de Pacotes

com.matheusluizroza.ecommerce_api
┣ 📂 config
┣ 📂 controller
┣ 📂 dto
┣ 📂 enums
┣ 📂 exceptionHandler
┣ 📂 filter
┣ 📂 model
┣ 📂 repository
┣ 📂 service
┗ EcommerceApiApplication.java

---

## 🔑 Funcionalidades

### 🧍 Usuários

- Cadastro
- Login
- Atualização de dados
- Alteração de senha
- Gerenciamento de roles (**ADMIN / CLIENTE**)

### 🛒 Produtos

- Criar
- Atualizar
- Listar
- Buscar por ID
- Deletar

### 📦 Pedidos

- Criar pedido
- Pagar pedido
- Cancelar pedido
- Listar pedidos por status
- Listar pedidos por usuário

### 🔐 Autenticação JWT

- Geração de token
- Validação de token em rotas protegidas
- Controle de acesso baseado em roles

---

## 🐳 Deploy Profissional com Docker & Docker Compose

Esse projeto foi construído com foco em **DevOps moderno**, garantindo que o ambiente seja:

- **Reproduzível**
- **Portável**
- **Seguro**
- **Padronizado**
- **Executável com apenas um comando**

### ✔ Isolamento entre serviços

A API e o PostgreSQL rodam em containers separados, comunicando-se apenas pela **rede interna Docker**, bloqueando acessos externos ao banco.

### ✔ Variáveis de ambiente seguras

Nada sensível é commitado.  
O arquivo `.env` (ignorando pelo Git) contém:

DB_USER=dev_user
DB_PASS=senha_muito_segura
DB_NAME=ecommerce
JWT_SECRET_KEY=minha_chave_jwt_super_segura

### ✔ Executando tudo com um único comando

```bash
docker compose up --build

---

## 📌 Exemplos de Endpoints

- `POST /api/users/save-user` → Registrar usuário
- `POST /api/users/login` → Login e geração de token JWT
- `GET /api/products` → Listar todos os produtos
- `POST /api/products` _(ADMIN)_ → Criar novo produto
- `POST /api/order` → Criar pedido
- `PUT /api/order/pay/{id}` → Pagar pedido

```

---

## 📖 Documentação Swagger

Após rodar a aplicação, acesse:
👉 `http://localhost:8080/swagger-ui.html`

---

## 🧪 Testes

A aplicação conta com testes unitários utilizando:

- **JUnit 5**
- **Mockito**

---

## 👨‍💻 Autor

**Matheus Luiz (Math-l7)**
[GitHub](https://github.com/Math-l7)

```

```

```

```
