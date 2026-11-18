package br.com.fiap.metrosp.dto;
import br.com.fiap.metrosp.model.Funcionario;
public record FuncionarioResponse(Long id,String nome,String email,String cargo) {
 public static FuncionarioResponse from(Funcionario f){return new FuncionarioResponse(f.getId(),f.getNome(),f.getEmail(),f.getCargo());}
}
