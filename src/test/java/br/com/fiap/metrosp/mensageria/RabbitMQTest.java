package br.com.fiap.metrosp.mensageria;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class RabbitMQTest {
 @Test void publicaComRotaCorreta(){
  RabbitTemplate template=mock(RabbitTemplate.class);
  var produtor=new OcorrenciaPublicador(template);
  produtor.publicar(new OcorrenciaCriadaEvento(101L,1L,"Escada rolante"));
  verify(template).convertAndSend(eq(RabbitMQConfig.EXCHANGE),eq(RabbitMQConfig.ROTA),contains("ocorrenciaId=101"));
 }
 @Test void consumidorAceitaMensagem(){
  assertDoesNotThrow(()->new OcorrenciaConsumidor().consumir("ocorrenciaId=101;estacaoId=1;tipo=Teste"));
 }
}
