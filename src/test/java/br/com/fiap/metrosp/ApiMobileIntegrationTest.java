package br.com.fiap.metrosp;

import br.com.fiap.metrosp.model.Funcionario;
import br.com.fiap.metrosp.repository.FuncionarioRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Teste ponta a ponta sem chamadas externas: H2 + JWT + endpoints REST. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional // rollback: evento RabbitMQ AFTER_COMMIT não é enviado a um broker real
class ApiMobileIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired FuncionarioRepository funcionarios;
    @Autowired PasswordEncoder encoder;

    private String tokenAdmin() throws Exception {
        Funcionario f = new Funcionario();
        f.setNome("Administrador de teste");
        f.setCpf("11111111111");
        f.setEmail("admin-integracao@example.com");
        f.setCargo("Gestor");
        f.setSenha(encoder.encode("123"));
        funcionarios.saveAndFlush(f);
        String corpo = mvc.perform(post("/api/auth/login")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content("{\"email\":\"admin-integracao@example.com\",\"senha\":\"123\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.perfil").value("ADMIN"))
            .andExpect(jsonPath("$.token").isString())
            .andReturn().getResponse().getContentAsString();
        return JsonPath.read(corpo, "$.token");
    }

    @Test
    void usuarioPodeCadastrarLogarMasNaoAlterarEstacoes() throws Exception {
        mvc.perform(post("/api/auth/cadastro")
          .contentType(MediaType.APPLICATION_JSON)
          .content("{\"nome\":\"Passageiro FIAP\",\"email\":\"passageiro-integracao@example.com\",\"senha\":\"123\"}"))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.email").value("passageiro-integracao@example.com"));
        String corpo = mvc.perform(post("/api/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content("{\"email\":\"passageiro-integracao@example.com\",\"senha\":\"123\"}"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.perfil").value("USER"))
          .andReturn().getResponse().getContentAsString();
        String token = JsonPath.read(corpo, "$.token");
        mvc.perform(post("/api/estacoes")
          .header("Authorization", "Bearer " + token)
          .contentType(MediaType.APPLICATION_JSON)
          .content("{\"nome\":\"Teste\",\"ordem\":5,\"linha\":\"Azul\",\"ativa\":\"S\",\"sentido\":\"JAB\"}"))
          .andExpect(status().isForbidden());
        mvc.perform(get("/api/estacoes").param("page","0").param("size","5"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content.length()").value(5));
    }

    @Test
    void adminExecutaCrudEstacaoEOcorrencia() throws Exception {
        String token = tokenAdmin();
        String estacaoNova = mvc.perform(post("/api/estacoes")
          .header("Authorization", "Bearer " + token)
          .contentType(MediaType.APPLICATION_JSON)
          .content("{\"nome\":\"Estação Integração\",\"ordem\":33,\"linha\":\"Azul\",\"ativa\":\"S\",\"sentido\":\"JAB\"}"))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.nome").value("Estação Integração"))
          .andReturn().getResponse().getContentAsString();
        Long estacaoId = Long.valueOf(JsonPath.read(estacaoNova, "$.id").toString());
        mvc.perform(put("/api/estacoes/{id}", estacaoId)
          .header("Authorization", "Bearer " + token)
          .contentType(MediaType.APPLICATION_JSON)
          .content("{\"nome\":\"Estação Atualizada\",\"ordem\":33,\"linha\":\"Azul\",\"ativa\":\"S\",\"sentido\":\"JAB\"}"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.nome").value("Estação Atualizada"));
        mvc.perform(get("/api/estacoes/{id}", estacaoId))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.nome").value("Estação Atualizada"));

        Long funcionarioId = funcionarios.findByEmailIgnoreCase("admin-integracao@example.com").orElseThrow().getId();
        String ocorrenciaJson = "{\"tipoProblema\":\"Escada parada\",\"mensagem\":\"Teste de ocorrência\",\"status\":\"A\",\"dataInicio\":\"2026-11-17T10:00:00\",\"estacaoId\":"+estacaoId+",\"funcionarioId\":"+funcionarioId+"}";
        String resposta = mvc.perform(post("/api/ocorrencias")
          .header("Authorization", "Bearer " + token)
          .contentType(MediaType.APPLICATION_JSON).content(ocorrenciaJson))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.tipoProblema").value("Escada parada"))
          .andReturn().getResponse().getContentAsString();
        Long ocorrenciaId = Long.valueOf(JsonPath.read(resposta, "$.id").toString());
        String update = ocorrenciaJson.replace("Escada parada", "Escada reparada").replace("\"status\":\"A\"", "\"status\":\"R\"");
        mvc.perform(put("/api/ocorrencias/{id}", ocorrenciaId)
          .header("Authorization", "Bearer " + token)
          .contentType(MediaType.APPLICATION_JSON).content(update))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.status").value("R"));
        mvc.perform(get("/api/ocorrencias/{id}", ocorrenciaId))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.tipoProblema").value("Escada reparada"));
        mvc.perform(delete("/api/ocorrencias/{id}", ocorrenciaId)
          .header("Authorization", "Bearer " + token))
          .andExpect(status().isNoContent());
        mvc.perform(delete("/api/estacoes/{id}", estacaoId)
          .header("Authorization", "Bearer " + token))
          .andExpect(status().isNoContent());
        mvc.perform(get("/api/estacoes/{id}", estacaoId))
          .andExpect(status().isNotFound());
    }

    @Test
    void validaDtoESeguranca() throws Exception {
        mvc.perform(get("/api/usuarios/1"))
            .andExpect(status().isUnauthorized());
        String token = tokenAdmin();
        mvc.perform(post("/api/estacoes")
          .header("Authorization", "Bearer " + token)
          .contentType(MediaType.APPLICATION_JSON)
          .content("{\"nome\":\"\",\"ordem\":0,\"ativa\":\"X\"}"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.campos.nome").exists())
          .andExpect(jsonPath("$.campos.ordem").exists());
    }
}
