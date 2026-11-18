package br.com.fiap.metrosp.dto;
import br.com.fiap.metrosp.model.Usuario;
public record UsuarioResponse(Long id,String nome,String email) {
 public static UsuarioResponse from(Usuario u){return new UsuarioResponse(u.getId(),u.getNome(),u.getEmail());}
}
