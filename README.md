# RestAll API

Um sistema completo e robusto de **Gestão de Restaurantes** (RESTful API), desenvolvido com as tecnologias mais modernas do ecossistema Java.

## Contexto e Visão Geral
O **RestAll** foi projetado para resolver a complexidade do dia a dia de estabelecimentos gastronômicos. A aplicação fornece um backend seguro, ágil e escalável para gerenciar todo o ciclo de vida do atendimento: desde o gerenciamento do **Cardápio** até o controle de **Comandas**, lançamento de **Pedidos** e gestão de **Usuários**.

Tudo isso suportado por uma arquitetura bem dividida em domínios, segurança via JWT, documentação automatizada via Swagger e uma base sólida de testes de integração usando Testcontainers.

##  Principais Funcionalidades
- ** Gestão de Cardápio:** Cadastro, edição e consulta de itens (produtos, preços, descrições) disponíveis no restaurante.
- ** Gestão de Comandas e Pedidos:** Abertura e fechamento de comandas por mesa ou cliente, e controle detalhado de cada pedido vinculado.
- ** Autenticação e Segurança:** Controle de acesso baseado em perfis (roles) utilizando Spring Security e JSON Web Tokens (JJWT).
- ** Gestão de Equipe e Restaurante:** Administração centralizada de funcionários (usuários) e das configurações base do estabelecimento.
- ** Tratamento Global de Erros:** Respostas de erro padronizadas e amigáveis, facilitando imensamente a vida de quem for consumir a API no Frontend.

##  Tecnologias Utilizadas
- **Linguagem:** Java 21
- **Framework Base:** Spring Boot (Web, Data JPA, Security, Validation)
- **Banco de Dados:** PostgreSQL
- **Migrações:** Flyway (Controle de versão do banco de dados)
- **Autenticação:** JWT (jjwt)
- **Testes:** JUnit 5, Spring Boot Test, e Testcontainers (para testes de integração com banco real)
- **Documentação:** Springdoc OpenAPI (Swagger UI)
- **Utilitários de Código:** Lombok (redução de boilerplate)

##  Estrutura do Projeto
A aplicação foi dividida em pacotes por domínio (Feature-based), promovendo alta coesão e baixo acoplamento:

```text
src/main/java/com/br/RestAll/
 ├── autenticacao/    # Lógica de login, geração e validação de tokens JWT
 ├── cardapio/        # Entidades, DTOs e Controllers dos itens do menu
 ├── comanda/         # Gerenciamento de comandas abertas e pedidos
 ├── comum/           # Configurações globais (ex: GlobalExceptionHandler, configs do Swagger)
 ├── restaurante/     # Informações core e configurações do estabelecimento
 └── usuario/         # Gestão dos usuários do sistema (administradores, garçons, caixas)
```

##  Como Executar Localmente

### Pré-requisitos
- **Java 21** instalado
- **Maven** instalado
- **Docker** em execução (necessário para rodar os Testes de Integração com Testcontainers e opcional para subir um banco rápido para dev)

### Passo a Passo

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/RestAll.git
   cd RestAll
   ```

2. **Configuração das Variáveis de Ambiente (.env):**
   Crie um arquivo `.env` na raiz do projeto para configurar o acesso ao banco e o JWT. Exemplo de conteúdo:
   ```env
   DB_URL=jdbc:postgresql://localhost:5432/restall_db
   DB_USERNAME=usuario
   DB_PASSWORD=senha
   JWT_SECRET=sua-chave-secreta-muito-segura-aqui
   ```

3. **Configure o Banco de Dados:**
   Certifique-se de ter uma instância do PostgreSQL rodando. Se preferir, use o Docker:
   ```bash
   docker run --name restall-postgres -e POSTGRES_USER=usuario -e POSTGRES_PASSWORD=senha -e POSTGRES_DB=restall_db -p 5432:5432 -d postgres
   ```
   *Nota: Ajuste as credenciais no seu `src/main/resources/application.properties` (ou `.yml`) para corresponderem ao banco acima.*

4. **Inicie a Aplicação:**
   O Spring Boot com o Flyway irá criar e atualizar automaticamente as tabelas do banco de dados ao inicializar.
   ```bash
   mvn spring-boot:run
   ```

5. **Explore a API (Swagger):**
   Com a aplicação em execução, abra o navegador e acesse a documentação interativa para testar os endpoints:
    [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) *(a porta pode variar conforme sua configuração)*.

##  Como Rodar os Testes
O projeto conta com testes robustos utilizando **Testcontainers**. Isso significa que, durante os testes, a aplicação irá subir automaticamente um contêiner temporário e isolado do PostgreSQL para validar se as integrações de banco de dados (Repository, JPA) funcionam perfeitamente.

Para rodar a suíte completa de testes, execute:
```bash
mvn clean test
```
*(Lembre-se de deixar o Docker aberto e rodando no seu computador).*

##  CI/CD e Deploy na AWS (EC2)
O projeto conta com uma esteira automatizada de **Integração e Entrega Contínua (CI/CD)**.
- **Integração Contínua (CI):** A cada novo push, os testes automatizados (incluindo o Testcontainers) são executados, garantindo a integridade e qualidade do código.
- **Deploy Contínuo (CD):** Após a validação, a aplicação é empacotada e o deploy é realizado de forma automática em uma instância **EC2 na AWS**, garantindo que a versão mais recente e estável esteja sempre disponível em produção.

## Licença

Este projeto está licenciado sob a [GNU General Public License v3.0] (LICENSE).

---
Desenvolvido por Matheus de Assis com Java e as melhores práticas de Engenharia de Software.