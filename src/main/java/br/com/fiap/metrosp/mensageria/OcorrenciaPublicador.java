package br.com.fiap.metrosp.mensageria;
import org.slf4j.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.*;
@Component
public class OcorrenciaPublicador {
 private static final Logger log=LoggerFactory.getLogger(OcorrenciaPublicador.class);
 private final RabbitTemplate template;
 public OcorrenciaPublicador(RabbitTemplate template){this.template=template;}
 /** Publica no broker somente apos COMMIT do registro. */
 @TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
 public void publicar(OcorrenciaCriadaEvento evento){
  String mensagem="ocorrenciaId="+evento.ocorrenciaId()+";estacaoId="+evento.estacaoId()+";tipo="+evento.tipoProblema();
  template.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROTA, mensagem);
  log.info("RabbitMQ publicou evento de ocorrencia {}",evento.ocorrenciaId());
 }
}
