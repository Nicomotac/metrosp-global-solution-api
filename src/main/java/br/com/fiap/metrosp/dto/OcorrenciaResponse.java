package br.com.fiap.metrosp.dto;
import br.com.fiap.metrosp.model.Ocorrencia;
import java.time.LocalDateTime;
public record OcorrenciaResponse(Long id,String tipoProblema,String mensagem,String status,
 LocalDateTime dataInicio,LocalDateTime dataFim,Long estacaoId,String estacao,
 Long funcionarioId,String funcionario) {
 public static OcorrenciaResponse from(Ocorrencia o) {
  return new OcorrenciaResponse(o.getId(),o.getTipoProblema(),o.getMensagem(),o.getStatus(),
    o.getDataInicio(),o.getDataFim(),o.getEstacao().getId(),o.getEstacao().getNome(),
    o.getFuncionario().getId(),o.getFuncionario().getNome());
 }
}
