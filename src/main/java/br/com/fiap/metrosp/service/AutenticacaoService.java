package br.com.fiap.metrosp.service;
import br.com.fiap.metrosp.dto.*;
import br.com.fiap.metrosp.repository.*;
import br.com.fiap.metrosp.exception.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.List;
@Service
public class AutenticacaoService {
 private final UsuarioRepository usuarios; private final FuncionarioRepository funcionarios;
 private final PasswordEncoder encoder;private final JwtEncoder jwtEncoder;
 private final String emissor;private final long minutos;
 public AutenticacaoService(UsuarioRepository usuarios,FuncionarioRepository funcionarios,PasswordEncoder encoder,JwtEncoder jwtEncoder,
  @Value("${app.jwt.issuer}") String emissor,@Value("${app.jwt.expiration-minutes}") long minutos){
  this.usuarios=usuarios;this.funcionarios=funcionarios;this.encoder=encoder;this.jwtEncoder=jwtEncoder;this.emissor=emissor;this.minutos=minutos;
 }
 @Transactional(readOnly=true) public LoginResponse entrar(LoginRequest d){
  var email=d.email().trim();var usuario=usuarios.findByEmailIgnoreCase(email);var funcionario=funcionarios.findByEmailIgnoreCase(email);
  if(usuario.isPresent() && funcionario.isPresent())throw new ConflitoException("E-mail cadastrado em dois perfis; solicite correção");
  if(usuario.isPresent() && encoder.matches(d.senha(),usuario.get().getSenha())){
   var u=usuario.get();return emitir("USER",u.getId(),u.getNome(),"ROLE_USER");
  }
  if(funcionario.isPresent() && funcionario.get().getSenha()!=null && encoder.matches(d.senha(),funcionario.get().getSenha())){
   var f=funcionario.get();return emitir("ADMIN",f.getId(),f.getNome(),"ROLE_ADMIN");
  }
  throw new BadCredentialsException("Credenciais inválidas");
 }
 private LoginResponse emitir(String perfil,Long id,String nome,String role){
  Instant inicio=Instant.now();
  var claims=JwtClaimsSet.builder().issuer(emissor).subject(perfil+":"+id)
   .issuedAt(inicio).expiresAt(inicio.plus(Duration.ofMinutes(minutos)))
   .claim("roles",List.of(role)).claim("perfil",perfil).build();
  var header=JwsHeader.with(MacAlgorithm.HS256).build();
  String token=jwtEncoder.encode(JwtEncoderParameters.from(header,claims)).getTokenValue();
  return new LoginResponse(token,"Bearer",perfil,id,nome);
 }
}
