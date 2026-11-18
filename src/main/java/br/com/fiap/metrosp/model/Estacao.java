package br.com.fiap.metrosp.model;
import jakarta.persistence.*;
@Entity @Table(name="T_ESTACAO")
public class Estacao {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="estacao_seq")
 @SequenceGenerator(name="estacao_seq", sequenceName="SEQ_ESTACAO", allocationSize=1)
 @Column(name="ID_ESTACAO",nullable=false) private Long id;
 @Column(name="NM_ESTACAO",nullable=false,length=200) private String nome;
 @Column(name="ORDEM",nullable=false) private Integer ordem;
 @Column(name="LINHA",length=30) private String linha;
 @Column(name="ATIVA",length=1) private String ativa;
 @Column(name="SENTIDO",length=3) private String sentido;
 public Long getId(){return id;} public String getNome(){return nome;} public void setNome(String v){nome=v;}
 public Integer getOrdem(){return ordem;} public void setOrdem(Integer v){ordem=v;}
 public String getLinha(){return linha;} public void setLinha(String v){linha=v;}
 public String getAtiva(){return ativa;} public void setAtiva(String v){ativa=v;}
 public String getSentido(){return sentido;} public void setSentido(String v){sentido=v;}
}
