package br.com.fiap.metrosp.service;
import br.com.fiap.metrosp.dto.*;
import br.com.fiap.metrosp.model.*;
import br.com.fiap.metrosp.repository.*;
import br.com.fiap.metrosp.exception.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class EstacaoService {
 private final EstacaoRepository repo;
 private final OcorrenciaRepository ocorrencias;
 public EstacaoService(EstacaoRepository repo,OcorrenciaRepository ocorrencias){this.repo=repo;this.ocorrencias=ocorrencias;}
 @Transactional(readOnly=true) public Page<EstacaoResponse> listar(Pageable p){return repo.findAll(p).map(EstacaoResponse::from);}
 @Transactional(readOnly=true) public EstacaoResponse buscar(Long id){return EstacaoResponse.from(encontrar(id));}
 public Estacao encontrar(Long id){return repo.findById(id).orElseThrow(()->new RecursoNaoEncontradoException("Estação não encontrada: "+id));}
 @Transactional public EstacaoResponse criar(EstacaoRequest dto){Estacao e=new Estacao();aplicar(e,dto);return EstacaoResponse.from(repo.save(e));}
 @Transactional public EstacaoResponse alterar(Long id,EstacaoRequest dto){Estacao e=encontrar(id);aplicar(e,dto);return EstacaoResponse.from(repo.save(e));}
 @Transactional public void excluir(Long id){encontrar(id);if(ocorrencias.existsByEstacaoId(id))throw new ConflitoException("Estação tem ocorrências vinculadas");repo.deleteById(id);}
 private void aplicar(Estacao e,EstacaoRequest d){e.setNome(d.nome());e.setOrdem(d.ordem());e.setLinha(d.linha());e.setAtiva(d.ativa());e.setSentido(d.sentido());}
}
