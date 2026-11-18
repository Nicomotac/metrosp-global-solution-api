package br.com.fiap.metrosp.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableMethodSecurity // Necessario para ativar @PreAuthorize em UsuarioController e outros endpoints.
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean JwtAuthenticationConverter jwtAuthenticationConverter(){
  JwtGrantedAuthoritiesConverter conv=new JwtGrantedAuthoritiesConverter();
  conv.setAuthoritiesClaimName("roles");conv.setAuthorityPrefix("");
  JwtAuthenticationConverter converter=new JwtAuthenticationConverter();converter.setJwtGrantedAuthoritiesConverter(conv);
  return converter;
 }
 @Bean SecurityFilterChain filterChain(HttpSecurity http,JwtAuthenticationConverter converter,
    org.springframework.security.web.AuthenticationEntryPoint authenticationEntryPoint,
    org.springframework.security.web.access.AccessDeniedHandler accessDeniedHandler) throws Exception{
  return http.csrf(csrf->csrf.disable())
   .exceptionHandling(ex->ex.authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler))
   .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(auth->auth
    .requestMatchers("/api/auth/**","/login","/swagger-ui.html","/swagger-ui/**","/v3/api-docs/**","/actuator/health").permitAll()
    .requestMatchers(HttpMethod.GET,"/api/estacoes/**","/api/ocorrencias","/api/ocorrencias/*").permitAll()
    .requestMatchers("/api/funcionarios/**").hasRole("ADMIN")
    .requestMatchers(HttpMethod.POST,"/api/estacoes/**","/api/ocorrencias/**").hasRole("ADMIN")
    .requestMatchers(HttpMethod.PUT,"/api/estacoes/**","/api/ocorrencias/**").hasRole("ADMIN")
    .requestMatchers(HttpMethod.DELETE,"/api/estacoes/**","/api/ocorrencias/**").hasRole("ADMIN")
    .anyRequest().authenticated())
   .oauth2ResourceServer(oauth2->oauth2.jwt(jwt->jwt.jwtAuthenticationConverter(converter)))
   .build();
 }
}
