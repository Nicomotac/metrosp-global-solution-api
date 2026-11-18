package br.com.fiap.metrosp.service;
import br.com.fiap.metrosp.dto.*;
import br.com.fiap.metrosp.model.*;
import br.com.fiap.metrosp.repository.*;
import br.com.fiap.metrosp.exception.*;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class UsuarioService {
 private final UsuarioRepository repo;private final PasswordEncoder encoder;private final IdentidadeService identidade;
 public UsuarioService(UsuarioRepository repo,PasswordEncoder encoder,IdentidadeService identidade){this.repo=repo;this.encoder=encoder;this.identidade=identidade;}
 @Transactional(readOnly=true) public Page<UsuarioResponse> listar(Pageable p){return repo.findAll(p).map(UsuarioResponse::from);}
 @Transactional(readOnly=true) public UsuarioResponse buscar(Long id){return UsuarioResponse.from(encontrar(id));}
 public Usuario encontrar(Long id){return repo.findById(id).orElseThrow(()->new RecursoNaoEncontradoException("Usuário não encontrado"));}
 @Transactional public UsuarioResponse criar(UsuarioRequest d){identidade.emailDisponivel(d.email(),null,null); Usuario u=new Usuario();u.setNome(d.nome());u.setEmail(d.email().trim().toLowerCase());u.setSenha(encoder.encode(d.senha()));return UsuarioResponse.from(repo.save(u));}
 @Transactional public UsuarioResponse alterar(Long id,UsuarioUpdate d){Usuario u=encontrar(id);identidade.emailDisponivel(d.email(),id,null);u.setNome(d.nome());u.setEmail(d.email().trim().toLowerCase());if(d.senha()!=null&&!d.senha().isBlank())u.setSenha(encoder.encode(d.senha()));return UsuarioResponse.from(repo.save(u));}
 @Transactional public void excluir(Long id){encontrar(id);repo.deleteById(id);}
}
