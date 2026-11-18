package br.com.fiap.metrosp.model;
import jakarta.persistence.*;
@Entity @Table(name="T_USUARIO")
public class Usuario {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="usuario_seq")
 @SequenceGenerator(name="usuario_seq",sequenceName="SEQ_USUARIO",allocationSize=1)
 @Column(name="ID_USUARIO",nullable=false) private Long id;
 @Column(name="NM_USUARIO",nullable=false,length=200) private String nome;
 @Column(name="EMAIL",nullable=false,length=200) private String email;
 @Column(name="SENHA",nullable=false,length=200) private String senha;
 public Long getId(){return id;} public String getNome(){return nome;} public void setNome(String v){nome=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getSenha(){return senha;} public void setSenha(String v){senha=v;}
}
