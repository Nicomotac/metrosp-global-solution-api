package br.com.fiap.metrosp.security;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
@Component
public class AccountAuthorization {
 public void somenteProprioOuFuncionario(Long id,Authentication auth){
  if(auth!=null&&auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")))return;
  if(auth instanceof JwtAuthenticationToken jwt && ("USER:"+id).equals(jwt.getToken().getSubject()))return;
  throw new AccessDeniedException("Sem permissão para acessar este usuário");
 }
}
