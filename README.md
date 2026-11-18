# MetroSP — Global Solution FIAP 2026/2

API REST Java 21 + Spring Boot para acompanhamento de estações e ocorrências do Metrô de São Paulo (CitizenTech). **A mesma API atende ao aplicativo React Native e à disciplina DevOps Tools & Cloud Computing.**

## Requisitos Java Advanced

| Exigência | Implementação |
|---|---|
| 3+ entidades relacionadas | `Estacao`, `Ocorrencia`, `Funcionario`; adicionalmente `Usuario`, todas mapeadas por JPA |
| CRUD, Spring Data JPA e paginação | Controllers/services/repositories das 4 entidades, com `Pageable` |
| Bean Validation e erro padronizado | DTOs com validações, `ApiExceptionHandler` (`@RestControllerAdvice`) |
| Login e dois papéis | Spring Security, BCrypt, JWT HS256; **ADMIN** e **USER** |
| Spring AI contextual | `GET /api/ocorrencias/{id}/orientacao`: contexto de uma ocorrência real no banco, não chat genérico |
| Arquitetura distribuída | RabbitMQ: evento publicado após commit de ocorrência e consumido de forma assíncrona |
| Swagger e Mobile | `/swagger-ui.html`, login JSON, DTOs e exemplos em [docs/INTEGRACAO-MOBILE.md](docs/INTEGRACAO-MOBILE.md) |

## Funcionamento para o Mobile

- `POST /api/auth/cadastro` cria passageiro **USER**.
- `POST /api/auth/login` ou `/login` recebe `{"email":"...","senha":"..."}` e retorna `{"token":"<jwt>","tipo":"Bearer","perfil":"USER" ou "ADMIN","id":...,"nome":"..."}`.
- ADMIN corresponde a funcionário/gestor na tabela `T_FUNCIONARIO`. Possui CRUD de estações, ocorrências, funcionários e gestão de usuários.
- USER pode consultar estações/ocorrências, obter orientação de IA e editar/excluir **apenas seu próprio perfil**.
- JWT é enviado em `Authorization: Bearer <token>`. A API não delega a autenticação ao aplicativo.
- Lista detalhada de rotas, exemplos de JSON, paginação e erros: [docs/INTEGRACAO-MOBILE.md](docs/INTEGRACAO-MOBILE.md).

## Executar localmente (Java 21)

1. Importe a pasta do projeto como **Maven > Existing Maven Projects** no Eclipse, ou abra no IntelliJ.
2. Use JDK 21, Maven Wrapper (Windows: `mvnw.cmd`; Linux/macOS: `./mvnw`).
3. Inicie o RabbitMQ local com `docker compose up -d` (somente o broker). Usuário local `metrosp`; senha de exemplo de desenvolvimento consta em `compose.yaml`, não reutilize em nuvem.
4. Use `SPRING_PROFILES_ACTIVE=local` (H2 local), `SPRING_AI_MODEL_CHAT=none` e `RABBITMQ_*` conforme `.env.example`.
5. Execute `./mvnw spring-boot:run` (ou `mvnw.cmd spring-boot:run`). O Flyway cria 4 tabelas e 46 registros de estação em banco H2 novo.
6. Para primeiro ADMIN em banco vazio, configure temporariamente `APP_BOOTSTRAP_ENABLED=true`, `APP_BOOTSTRAP_EMAIL`, `APP_BOOTSTRAP_PASSWORD` (12+ caracteres), `APP_BOOTSTRAP_CPF` (11 dígitos), inicie uma vez, **depois volte a `false`**. Nunca inclua credenciais padrão no Git.
7. Swagger: `http://localhost:8080/swagger-ui.html`; health: `http://localhost:8080/actuator/health`.

## Azure Web App — configuração obrigatória

Infraestrutura existente:

| Campo | Nome |
|---|---|
| Resource Group | `rg-GLOBAL-mobile-rm561857` |
| App Service Plan | `plan-API-mobile-rm561857` |
| Web App | `app-global-mobile-rm561859` |
| Localização e runtime | `brazilsouth`, Linux Java 21, plano F1 |

O script `scripts/azure-criar-recursos.sh` cria ou reutiliza **somente o App Service Plan e Web App**, sem senhas. O Oracle institucional e o RabbitMQ **não** são criados pelo script.

Configure no Portal Azure > Web App > **Variáveis de ambiente** (não comite seus valores):

| Variável | Obrigatória? | Uso |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | sim | `oracle` (ou `azure`, alias Oracle) |
| `SPRING_DATASOURCE_URL` | sim | URL JDBC Oracle FIAP (sem credenciais) |
| `SPRING_DATASOURCE_USERNAME` | sim | Conta Oracle |
| `SPRING_DATASOURCE_PASSWORD` | sim | Senha Oracle **rotacionada** |
| `JWT_SECRET` | sim | Segredo aleatório HS256 de **32 bytes ou mais**, diferente de qualquer senha |
| `RABBITMQ_HOST` | sim | Broker RabbitMQ alcançável pelo App Service |
| `RABBITMQ_PORT` | sim | Em geral 5671 com TLS; conforme broker |
| `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` | sim | Credenciais broker externo |
| `RABBITMQ_SSL_ENABLED` | sim | `true` se o broker usa TLS |
| `OPENAI_API_KEY` | para IA real | Chave **nova** e válida; nunca registrar em log |
| `SPRING_AI_MODEL_CHAT` | para IA real | `openai` para ativar a chamada; `none` sem LLM |
| `OPENAI_MODEL` | opcional | Modelo selecionado, padrão `gpt-4o-mini` |
| `APP_BOOTSTRAP_ENABLED` e `APP_BOOTSTRAP_*` | só primeira execução | Criar ADMIN inicial em banco vazio, depois desabilitar/remover |
| `SPRING_FLYWAY_ENABLED` | opcional | `false` **somente** quando já existe esquema equivalente validado, sem rodar migrations |

### Atenção à estrutura Oracle

As migrations em `src/main/resources/db/migration/oracle/` estão desenhadas para **schema novo/vazio**. Se o schema FIAP possuir tabelas anteriores, o Flyway pode interromper o startup. **Não ative `baseline-on-migrate` automaticamente e não rode `script-bd.sql` sobre tabelas existentes**. Se o banco já vier pronto, valide nomes de tabelas, colunas, FKs e sequências com as classes JPA e, somente depois, considere desativar o Flyway com `SPRING_FLYWAY_ENABLED=false`.

> **Ponto a confirmar com o professor**: o Oracle da FIAP é um servidor remoto, mas o edital de DevOps pede banco em nuvem via container ou PaaS. A hospedagem institucional não comprova automaticamente essa condição. Confirme a aceitação antes da apresentação.

### RabbitMQ e IA

O `compose.yaml` inicia RabbitMQ **somente no computador**. Para demonstrar RabbitMQ no Azure, é necessário um broker acessível pela nuvem, preferencialmente AMQPS/TLS. Eventos são publicados depois do commit do CRUD e consumidos pelo `@RabbitListener`. O F1 é um ambiente de demonstração limitado e pode ter reinicializações; não é solução de alta disponibilidade.

A IA usa dados de uma ocorrência persistida, não faz previsões oficiais. O endpoint requer JWT e crédito da conta OpenAI separado do crédito Azure. `spring.ai.model.chat` é controlado por `SPRING_AI_MODEL_CHAT` e `OPENAI_API_KEY` é lido por `spring.ai.openai.api-key`.

## Tests

```bash
# Windows
mvnw.cmd clean verify
# Linux/macOS
./mvnw clean verify
```

- `FlywayLoginTest`: migrations H2, carga inicial e login JWT.
- `ApiMobileIntegrationTest`: login ADMIN/USER, restrições 401/403, validações, CRUDs de estação e ocorrência com MockMvc + H2.
- `RabbitMQTest`: publicação por RabbitTemplate simulado e consumo do listener; o broker **real** ainda requer demonstração manual/integração na nuvem.
- CI publica relatórios JUnit e JAR.

## DevOps e entrega

- `scripts/azure-criar-recursos.sh`: provisionamento Azure CLI do Web App F1.
- `scripts/script-bd.sql`: DDL Oracle das tabelas da aplicação.
- `azure-pipeline.yml`: **Build** em merge na main, com testes e artefato, e **Release** automática para o Web App. **Antes de rodar**, substituir `CONFIGURAR-NOME-SERVICE-CONNECTION` pelo nome real da conexão Azure Resource Manager.
- Boards Scrum / Azure Repos: branch `main` protegida, tarefa vinculada a branch/commit/PR, professor com acesso **Basic + Project Administrators**.
- Vídeo único contínuo: alteração **no código Java**, PR e merge, Build e Release, execução dos CRUDs de duas tabelas, SELECT direto no banco, evidências RabbitMQ e IA, narração e 720p mínimo.
- Desenho macro: [docs/ARQUITETURA.md](docs/ARQUITETURA.md).

**Validação pendente**: compilação em Java 21 com dependências Maven, conexão Oracle/Flyway real, acesso ao broker RabbitMQ, chamada real à OpenAI e execução de CI/CD no Azure. Nenhum teste de rede externo foi simulado como concluído.

### Senhas de demonstração acadêmica

Por solicitação para facilitar os testes de login do Mobile, a validação de tamanho de senha de usuários e funcionários foi reduzida para **3 a 72 caracteres** (inclusive bootstrap de ADMIN). `123` funciona apenas como exemplo de teste; não use essa senha em contas reais ou no Web App público. O armazenamento segue com BCrypt e a autenticação JWT/controle de papéis continua ativa. `JWT_SECRET` exige pelo menos 32 bytes e **não deve** ser enfraquecida. Senhas de Oracle, RabbitMQ e chaves OpenAI são gerenciadas externamente e não devem ser versionadas.
