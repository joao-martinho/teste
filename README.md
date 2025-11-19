## Teste para desenvolvedor _back-end_ Java

Este repositório contém a minha solução de um teste para uma vaga de desenvolvedor: um CRUD de produtos e serviços obedecendo a certas regras de negócio feito com Spring Boot. O teste tinha três níveis de dificuldade a serem escolhidos pelo avaliado, e eu escolhi o nível II. Abaixo estão instruções detalhadas de como executar o projeto.

## Pré-requisitos

- **Java 17** ou superior  
  Verifique a instalação:  

  ```bash
  java -version
  ```
- **Maven**  
  Verifique a instalação:  

  ```bash
  mvn -version
  ```
- **PostgreSQL**  
  Instale e crie um banco de dados para o projeto. No `psql`:

  ```sql
  CREATE DATABASE nome_do_banco;
  ```

## Configuração do banco de dados

Edite o arquivo `src/main/resources/application.properties` com as credenciais e a URL do seu banco de dados PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_banco
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Ajuste porta, usuário e senha conforme necessário.

## Executando o _back-end_ (Spring Boot)

### Opção A — Pela IDE

1. Abra o projeto no IntelliJ, Spring Tools Suite ou VS Code, etc;  
2. Localize a classe principal, que contém o método `public static void main`;  
3. Execute a aplicação.

### Opção B — Via Maven (linha de comando)

No diretório raiz do projeto:

```bash
mvn spring-boot:run
```

### Opção C — Gerar JAR e executar

```bash
mvn clean package
java -jar target/nomedoarquivo.jar
```

O back-end ficará disponível em **http://localhost:8080**.

## Testes automatizados

Execute todos os testes com o Maven:

```bash
mvn test
```
