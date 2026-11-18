package br.com.fiap.metrosp.mensageria;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.*;
/** Mesmo padrao do projeto de aula: fila + DirectExchange + chave de roteamento. */
@Configuration
public class RabbitMQConfig {
 public static final String FILA = "metrosp.ocorrencias.criadas";
 public static final String EXCHANGE = "metrosp.ocorrencias";
 public static final String ROTA = "ocorrencia.criada";
 @Bean public Queue filaOcorrencias() { return new Queue(FILA, true); }
 @Bean public DirectExchange exchangeOcorrencias() { return new DirectExchange(EXCHANGE, true, false); }
 @Bean public Binding bindingOcorrencias(Queue filaOcorrencias, DirectExchange exchangeOcorrencias) {
  return BindingBuilder.bind(filaOcorrencias).to(exchangeOcorrencias).with(ROTA);
 }
}
