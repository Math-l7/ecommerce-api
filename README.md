# 🛒 E-commerce API

API RESTful desenvolvida em **Spring Boot** para gerenciar usuários, produtos e pedidos, com autenticação via **JWT** e controle de acesso com **Spring Security**.

---

## 🚀 Tecnologias
- **Java 21**  
- **Spring Boot 3**  
- **Spring Security + JWT**  
- **Spring Data JPA**  
- **PostgreSQL**  
- **Maven**  
- **JUnit 5 + Mockito** (testes unitários)  
- **Swagger/OpenAPI**  

---

## 🔑 Funcionalidades
- **Usuários**: cadastro, autenticação, atualização e gerenciamento de roles (**ADMIN/CLIENTE**).  
- **Produtos**: criação, listagem, busca, atualização e exclusão.  
- **Pedidos**: criação, pagamento, cancelamento e busca por status/usuário.  
- **Autenticação JWT**: login e acesso seguro a endpoints protegidos.  

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

## 📌 Exemplos de Endpoints
- `POST /api/users/save-user` → Registrar usuário  
- `POST /api/users/login` → Login e geração de token JWT  
- `GET /api/products` → Listar todos os produtos  
- `POST /api/products` *(ADMIN)* → Criar novo produto  
- `POST /api/order` → Criar pedido  
- `PUT /api/order/pay/{id}` → Pagar pedido  

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
