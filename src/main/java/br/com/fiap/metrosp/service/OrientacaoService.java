package br.com.fiap.metrosp.service;
import br.com.fiap.metrosp.dto.OrientacaoResponse;
import br.com.fiap.metrosp.exception.RecursoNaoEncontradoException;
import br.com.fiap.metrosp.repository.OcorrenciaRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
@Service
public class OrientacaoService {
 private final OcorrenciaRepository ocorrencias;
 private final ObjectProvider<ChatClient.Builder> chatBuilders;
 public OrientacaoService(OcorrenciaRepository ocorrencias, ObjectProvider<ChatClient.Builder> chatBuilders){
  this.ocorrencias=ocorrencias;this.chatBuilders=chatBuilders;
 }
 @Transactional(readOnly=true)
 public OrientacaoResponse orientar(Long id){
  var o=ocorrencias.findById(id).orElseThrow(()->new RecursoNaoEncontradoException("Ocorrência não encontrada"));
  var builder=chatBuilders.getIfAvailable();
  if(builder==null) throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
   "Spring AI desativada: configure OPENAI_API_KEY e SPRING_AI_MODEL_CHAT=openai");
  var dados="Estação: "+o.getEstacao().getNome()+"; linha: "+o.getEstacao().getLinha()+
   "; sentido: "+o.getEstacao().getSentido()+"; tipo: "+o.getTipoProblema()+
   "; mensagem: "+o.getMensagem()+"; status: "+o.getStatus();
  try {
   String texto=builder.build().prompt()
    .system("Você é um orientador de passageiros do Metrô de São Paulo. Use somente os dados da ocorrência fornecida; não invente tempo real, previsão, linha alternativa ou informações oficiais. Oriente com cautela, em português, e recomende consultar canais oficiais do Metrô quando necessário. Responda em até 120 palavras.")
    .user(dados).call().content();
   return new OrientacaoResponse(id,texto);
  } catch(Exception ex){throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
   "Falha ao consultar modelo de IA. Verifique a chave, o modelo e os créditos.",ex);}
 }
}
