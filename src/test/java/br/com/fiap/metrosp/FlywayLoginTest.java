package br.com.fiap.metrosp;
import br.com.fiap.metrosp.dto.*;
import br.com.fiap.metrosp.repository.*;
import br.com.fiap.metrosp.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
/** Testa Flyway, persistencia H2, BCrypt e emissao JWT sem RabbitMQ/OpenAI. */
@SpringBootTest
@ActiveProfiles("test")
class FlywayLoginTest {
 @Autowired EstacaoRepository estacoes;
 @Autowired UsuarioRepository usuarios;
 @Autowired UsuarioService usuarioService;
 @Autowired AutenticacaoService autenticacaoService;
 @Test void migrouQuatroTabelasECarregouEstacoes(){
  assertEquals(46L,estacoes.count());
  assertTrue(estacoes.findById(1L).isPresent());
 }
 @Test void cadastroELoginRetornamJwt(){
  usuarioService.criar(new UsuarioRequest("Teste FIAP","teste-rabbit@exemplo.com","SenhaForte123!"));
  var resposta=autenticacaoService.entrar(new LoginRequest("teste-rabbit@exemplo.com","SenhaForte123!"));
  assertEquals("USER",resposta.perfil());
  assertNotNull(resposta.token());
  assertFalse(resposta.token().isBlank());
 }
}
