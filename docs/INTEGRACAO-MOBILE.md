# Contrato API — Aplicativo React Native / Expo

## Ambiente

- Base URL no Azure: `https://app-global-mobile-rm561859.azurewebsites.net`
- Base URL no Android Emulator (API local): `http://10.0.2.2:8080`
- Requisições/retornos JSON (`Content-Type: application/json`). Para endpoints protegidos, enviar `Authorization: Bearer <token>`.
- `id` vem do banco. Não assuma IDs fixos nem mantenha senha no AsyncStorage. O login é validado pela API, não no celular.

## Login: ADMIN e USER

`POST /api/auth/login` (ou `/login` por compatibilidade):

```json
{"email":"usuario@example.com","senha":"123"}
```

HTTP 200:

```json
{"token":"<jwt>","tipo":"Bearer","perfil":"USER","id":100,"nome":"Passageiro"}
```

- `perfil: "USER"`: cadastrado por `POST /api/auth/cadastro`; pode consultar estações/ocorrências e gerenciar o próprio perfil.
- `perfil: "ADMIN"`: registro na tabela de funcionários, gerado inicialmente com bootstrap; pode executar CRUD das estações, ocorrências e funcionários e consultar/gerenciar usuários.
- Se e-mail/senha forem inválidos: HTTP 401. Se token faltar em rota protegida: HTTP 401; se papel insuficiente: HTTP 403.
- Não existe endpoint público para cadastrar ADMIN, para não permitir elevação de privilégio.

## Cadastro de USER

`POST /api/auth/cadastro`:

```json
{"nome":"Passageiro FIAP","email":"passageiro@example.com","senha":"123"}
```

Retorna HTTP 201 com id, nome e e-mail sem senha.

## CRUDs para o Mobile

| Método e endpoint | Acesso | Observações |
|---|---|---|
| GET `/api/estacoes?page=0&size=10` | público | Page: `content`, `totalElements`, `totalPages`, `number` |
| GET `/api/estacoes/{id}` | público | estação |
| POST `/api/estacoes` | ADMIN | criação |
| PUT `/api/estacoes/{id}` | ADMIN | atualização |
| DELETE `/api/estacoes/{id}` | ADMIN | 204 |
| GET `/api/ocorrencias?estacaoId={id}&page=0&size=10` | público | listagem, filtro opcional |
| GET `/api/ocorrencias/{id}` | público | ocorrência |
| POST `/api/ocorrencias` | ADMIN | cria e publica evento RabbitMQ |
| PUT `/api/ocorrencias/{id}` | ADMIN | altera |
| DELETE `/api/ocorrencias/{id}` | ADMIN | 204 |
| GET `/api/usuarios/{id}` | próprio USER ou ADMIN | perfil |
| PUT `/api/usuarios/{id}` | próprio USER ou ADMIN | altera perfil |
| DELETE `/api/usuarios/{id}` | próprio USER ou ADMIN | exclui perfil |
| GET `/api/usuarios` | ADMIN | paginado |
| POST `/api/usuarios` | ADMIN | cria usuário |
| GET/POST/PUT/DELETE `/api/funcionarios` | ADMIN | funcionários, detalhe/edição/exclusão usam `/{id}` |
| GET `/api/ocorrencias/{id}/orientacao` | ADMIN ou USER | Spring AI contextual, exige chave válida |

Exemplo de criação de estação:

```json
{"nome":"Estação Teste","ordem":33,"linha":"Azul","ativa":"S","sentido":"JAB"}
```

Exemplo de ocorrência (IDs reais do banco, `dataInicio` ISO sem fuso):

```json
{"tipoProblema":"Escada rolante parada","mensagem":"Escada da estação indisponível","status":"A","dataInicio":"2026-11-17T14:00:00","dataFim":null,"estacaoId":1,"funcionarioId":100}
```

Tratamento de erros (exemplo de HTTP 400):

```json
{"instante":"2026-11-17T14:00:00Z","status":400,"erro":"Bad Request","mensagem":"Dados inválidos","campos":{"nome":"não deve estar em branco"}}
```

## Mobile

- Após login, guardar token de maneira segura no dispositivo e ler `perfil` para direcionar para a navegação ADMIN ou USER.
- Enviar token Bearer em todos os métodos protegidos.
- Reagir a HTTP 401 (sessão expirada) e 403 (permissão insuficiente) sem fingir sucesso.
- Nunca usar AsyncStorage como substituto de banco de dados para os CRUDs exigidos.

### Senhas de teste acadêmico

Para facilitar testes locais, a API aceita senhas de **3 a 72 caracteres** em cadastro e alteração de usuários e funcionários, e no bootstrap do primeiro administrador. Senhas como `123` são aceitas, mas **não devem ser usadas em contas reais ou em uma aplicação publicada na internet**. As senhas continuam armazenadas com BCrypt. A chave `JWT_SECRET` deve permanecer forte e separada das senhas de login.
