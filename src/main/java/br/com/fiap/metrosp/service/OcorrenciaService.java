package br.com.fiap.metrosp.service;
import br.com.fiap.metrosp.dto.*;
import br.com.fiap.metrosp.model.*;
import br.com.fiap.metrosp.repository.*;
import br.com.fiap.metrosp.exception.*;
import org.springframework.data.domain.*;
import org.springframework.context.ApplicationEventPublisher;
import br.com.fiap.metrosp.mensageria.OcorrenciaCriadaEvento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class OcorrenciaService {
 private final OcorrenciaRepository repo;
 private final EstacaoRepository estacoes;
 private final FuncionarioRepository funcionarios;
 private final ApplicationEventPublisher eventos;
 public OcorrenciaService(OcorrenciaRepository repo,EstacaoRepository estacoes,FuncionarioRepository funcionarios,ApplicationEventPublisher eventos){this.repo=repo;this.estacoes=estacoes;this.funcionarios=funcionarios;this.eventos=eventos;}
 @Transactional(readOnly=true) public Page<OcorrenciaResponse> listar(Long estacaoId,Pageable p){
  return (estacaoId==null?repo.findAll(p):repo.findByEstacaoId(estacaoId,p)).map(OcorrenciaResponse::from);
 }
 @Transactional(readOnly=true) public OcorrenciaResponse buscar(Long id){return OcorrenciaResponse.from(encontrar(id));}
 @Transactional(readOnly=true) public Ocorrencia encontrar(Long id){return repo.findById(id).orElseThrow(()->new RecursoNaoEncontradoException("Ocorrência não encontrada: "+id));}
 @Transactional public OcorrenciaResponse criar(OcorrenciaRequest dto){Ocorrencia o=new Ocorrencia();aplicar(o,dto);o=repo.saveAndFlush(o);OcorrenciaResponse resposta=OcorrenciaResponse.from(o);eventos.publishEvent(new OcorrenciaCriadaEvento(o.getId(),o.getEstacao().getId(),o.getTipoProblema()));return resposta;}
 @Transactional public OcorrenciaResponse alterar(Long id,OcorrenciaRequest dto){Ocorrencia o=encontrar(id);aplicar(o,dto);return OcorrenciaResponse.from(repo.save(o));}
 @Transactional public void excluir(Long id){encontrar(id);repo.deleteById(id);}
 private void aplicar(Ocorrencia o,OcorrenciaRequest d){
  if(d.dataFim()!=null && d.dataFim().isBefore(d.dataInicio()))throw new IllegalArgumentException("Data final anterior à inicial");
  o.setTipoProblema(d.tipoProblema());o.setMensagem(d.mensagem());o.setStatus(d.status());
  o.setDataInicio(d.dataInicio());o.setDataFim(d.dataFim());
  o.setEstacao(estacoes.findById(d.estacaoId()).orElseThrow(()->new RecursoNaoEncontradoException("Estação não encontrada")));
  o.setFuncionario(funcionarios.findById(d.funcionarioId()).orElseThrow(()->new RecursoNaoEncontradoException("Funcionário não encontrado")));
 }
}
