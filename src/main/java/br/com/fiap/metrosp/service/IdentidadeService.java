package br.com.fiap.metrosp.service;
import br.com.fiap.metrosp.repository.*;
import br.com.fiap.metrosp.exception.ConflitoException;
import org.springframework.stereotype.Service;
@Service
public class IdentidadeService {
 private final UsuarioRepository usuarios; private final FuncionarioRepository funcionarios;
 public IdentidadeService(UsuarioRepository usuarios,FuncionarioRepository funcionarios){this.usuarios=usuarios;this.funcionarios=funcionarios;}
 public void emailDisponivel(String email,Long usuarioAtual,Long funcionarioAtual){
  var u=usuarios.findByEmailIgnoreCase(email);
  var f=funcionarios.findByEmailIgnoreCase(email);
  if(u.isPresent() && !u.get().getId().equals(usuarioAtual) || f.isPresent() && !f.get().getId().equals(funcionarioAtual))
    throw new ConflitoException("E-mail já cadastrado");
 }
}
