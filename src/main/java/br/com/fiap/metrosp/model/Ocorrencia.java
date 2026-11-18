package br.com.fiap.metrosp.model;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
@Entity @Table(name="T_OCORRENCIA")
public class Ocorrencia {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="ocorrencia_seq")
 @SequenceGenerator(name="ocorrencia_seq",sequenceName="SEQ_OCORRENCIA",allocationSize=1)
 @Column(name="ID_OCORRENCIAN",nullable=false) private Long id;
 @Column(name="TIPO_PROBLEMA",nullable=false,length=100) private String tipoProblema;
 @JdbcTypeCode(SqlTypes.LONG32VARCHAR) @Column(name="MENSAGEM",nullable=false) private String mensagem;
 @Column(name="STATUS",nullable=false,length=1) private String status;
 @Column(name="DT_INICIO",nullable=false) private LocalDateTime dataInicio;
 @Column(name="DT_FIM") private LocalDateTime dataFim;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="FK_ID_FUNCIONARIO",nullable=false)
 private Funcionario funcionario;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="FK_ID_ESTACAO",nullable=false)
 private Estacao estacao;
 public Long getId(){return id;} public String getTipoProblema(){return tipoProblema;} public void setTipoProblema(String v){tipoProblema=v;}
 public String getMensagem(){return mensagem;} public void setMensagem(String v){mensagem=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public LocalDateTime getDataInicio(){return dataInicio;} public void setDataInicio(LocalDateTime v){dataInicio=v;}
 public LocalDateTime getDataFim(){return dataFim;} public void setDataFim(LocalDateTime v){dataFim=v;}
 public Funcionario getFuncionario(){return funcionario;} public void setFuncionario(Funcionario v){funcionario=v;}
 public Estacao getEstacao(){return estacao;} public void setEstacao(Estacao v){estacao=v;}
}
