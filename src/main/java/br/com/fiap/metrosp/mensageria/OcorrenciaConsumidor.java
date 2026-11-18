package br.com.fiap.metrosp.mensageria;
import org.slf4j.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
public class OcorrenciaConsumidor {
 private static final Logger log=LoggerFactory.getLogger(OcorrenciaConsumidor.class);
 /** Consumo assincrono demonstravel nos logs e no painel RabbitMQ. */
 @RabbitListener(queues=RabbitMQConfig.FILA)
 public void consumir(String mensagem){log.info("RabbitMQ CONSUMIU evento: {}",mensagem);}
}
