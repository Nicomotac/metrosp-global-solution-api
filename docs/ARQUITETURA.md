# Arquitetura macro — Global Solution Metrô SP

```mermaid
flowchart TD
  M[Aplicativo React Native] -->|HTTPS JSON / Bearer JWT| W[Azure Web App F1 Java 21]
  W --> API[Spring Boot Controller / Service / Spring Data JPA]
  API -->|JDBC Oracle / TLS se disponivel| DB[(Oracle FIAP - validar regra de nuvem com professor)]
  API -->|apos commit: RabbitTemplate| B[(RabbitMQ externo acessivel por AMQPS)]
  B -->|consumo assincrono| C[@RabbitListener no Web App]
  API -->|contexto de ocorrencia| AI[Spring AI ChatClient]
  AI -->|HTTPS com API key no ambiente| LLM[OpenAI]
  DEV[Azure Boards + Repos] --> CI[Azure Pipelines: Build + testes]
  CI -->|JAR| CD[Azure Pipelines: Release]
  CD -->|deploy automatizado| W
```

## Componentes

- **Mobile**: Login `ADMIN` (funcionário/gestor) e `USER` (passageiro); Bearer JWT. CRUD administrativo de estações e ocorrências, CRUD de perfil do usuário.
- **API**: quatro entidades Oracle (`T_ESTACAO`, `T_FUNCIONARIO`, `T_OCORRENCIA`, `T_USUARIO`); `T_OCORRENCIA` relaciona estação e funcionário. Spring Data JPA, paginação e validação DTO.
- **JWT**: `ROLE_ADMIN` e `ROLE_USER` com `@PreAuthorize` e filtro Security.
- **RabbitMQ**: após persistir a ocorrência, publicação da mensagem na exchange, roteamento para fila durável e consumo assíncrono no `@RabbitListener`. Broker externo requerido na Azure; Docker Compose somente local. Não implementar garantia outbox neste MVP.
- **Spring AI**: orientação obtida de ocorrência real e estação do banco. A saída não representa informação oficial em tempo real.
- **Infraestrutura**: `scripts/azure-criar-recursos.sh` provisiona apenas Resource Group, App Service Plan F1 e Web App. O Oracle FIAP é externo e não é criado pelo script; confirmar aceitação com o professor para a exigência de banco em nuvem.
- **Azure DevOps**: repositório privado, Boards Scrum (Task), política `main`, Build com testes e JAR, Release automática ao Web App.

## Limitações operacionais

O F1 oferece recursos restritos. O startup de Spring Boot + RabbitMQ + Spring AI pode exigir otimização ou plano maior. O Azure Web App **não** instala automaticamente broker RabbitMQ. Flyway, no modo padrão, exige um esquema Oracle novo compatível; nunca executar DDL por cima de tabelas existentes. Os requisitos de deploy e de banco só se comprovam com execução real.
