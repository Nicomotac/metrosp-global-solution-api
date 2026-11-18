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
public class FuncionarioService {
 private final FuncionarioRepository repo;private final OcorrenciaRepository ocorrencias;
 private final PasswordEncoder encoder;private final IdentidadeService identidade;
 public FuncionarioService(FuncionarioRepository repo,OcorrenciaRepository ocorrencias,PasswordEncoder encoder,IdentidadeService identidade){this.repo=repo;this.ocorrencias=ocorrencias;this.encoder=encoder;this.identidade=identidade;}
 @Transactional(readOnly=true) public Page<FuncionarioResponse> listar(Pageable p){return repo.findAll(p).map(FuncionarioResponse::from);}
 @Transactional(readOnly=true) public FuncionarioResponse buscar(Long id){return FuncionarioResponse.from(encontrar(id));}
 public Funcionario encontrar(Long id){return repo.findById(id).orElseThrow(()->new RecursoNaoEncontradoException("Funcionário não encontrado"));}
 @Transactional public FuncionarioResponse criar(FuncionarioRequest d){identidade.emailDisponivel(d.email(),null,null);Funcionario f=new Funcionario();f.setNome(d.nome());f.setEmail(d.email().trim().toLowerCase());f.setCpf(d.cpf());f.setSenha(encoder.encode(d.senha()));f.setCargo(d.cargo());return FuncionarioResponse.from(repo.save(f));}
 @Transactional public FuncionarioResponse alterar(Long id,FuncionarioUpdate d){Funcionario f=encontrar(id);identidade.emailDisponivel(d.email(),null,id);f.setNome(d.nome());f.setEmail(d.email().trim().toLowerCase());f.setCpf(d.cpf());f.setCargo(d.cargo());if(d.senha()!=null&&!d.senha().isBlank())f.setSenha(encoder.encode(d.senha()));return FuncionarioResponse.from(repo.save(f));}
 @Transactional public void excluir(Long id){encontrar(id);if(ocorrencias.existsByFuncionarioId(id))throw new ConflitoException("Funcionário tem ocorrências vinculadas");repo.deleteById(id);}
}
