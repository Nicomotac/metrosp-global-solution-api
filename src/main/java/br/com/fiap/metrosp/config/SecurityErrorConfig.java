package br.com.fiap.metrosp.config;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
@Configuration
public class SecurityErrorConfig {
 @Bean AuthenticationEntryPoint authenticationEntryPoint(){return (request,response,ex)->write(response,401,"Não autenticado");}
 @Bean AccessDeniedHandler accessDeniedHandler(){return (request,response,ex)->write(response,403,"Acesso negado");}
 private void write(HttpServletResponse response,int status,String message)throws java.io.IOException {
  response.setStatus(status);response.setContentType("application/json;charset=UTF-8");
  response.getWriter().write("{\"status\":"+status+",\"erro\":\"Segurança\",\"mensagem\":\""+message+"\"}");
 }
}
