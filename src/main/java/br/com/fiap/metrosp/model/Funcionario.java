package br.com.fiap.metrosp.model;
import jakarta.persistence.*;
@Entity @Table(name="T_FUNCIONARIO")
public class Funcionario {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="funcionario_seq")
 @SequenceGenerator(name="funcionario_seq",sequenceName="SEQ_FUNCIONARIO",allocationSize=1)
 @Column(name="ID_FUNCIONARIO",nullable=false) private Long id;
 @Column(name="NM_FUNCIONARIO",nullable=false,length=200) private String nome;
 @Column(name="CPF",nullable=false,length=11) private String cpf;
 @Column(name="EMAIL",length=200) private String email;
 @Column(name="SENHA",length=200) private String senha;
 @Column(name="CARGO",nullable=false,length=60) private String cargo;
 public Long getId(){return id;} public String getNome(){return nome;} public void setNome(String v){nome=v;}
 public String getCpf(){return cpf;} public void setCpf(String v){cpf=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getSenha(){return senha;} public void setSenha(String v){senha=v;}
 public String getCargo(){return cargo;} public void setCargo(String v){cargo=v;}
}
