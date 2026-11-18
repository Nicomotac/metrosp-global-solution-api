package br.com.fiap.metrosp.dto;
import br.com.fiap.metrosp.model.Estacao;
public record EstacaoResponse(Long id,String nome,Integer ordem,String linha,String ativa,String sentido) {
 public static EstacaoResponse from(Estacao e) {return new EstacaoResponse(e.getId(),e.getNome(),e.getOrdem(),e.getLinha(),e.getAtiva(),e.getSentido());}
}
