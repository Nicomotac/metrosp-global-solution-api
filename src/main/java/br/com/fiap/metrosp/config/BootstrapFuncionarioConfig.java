package br.com.fiap.metrosp.config;

import br.com.fiap.metrosp.model.Funcionario;
import br.com.fiap.metrosp.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Cria o primeiro funcionario somente quando autorizado por variavel, em base nova. */
@Configuration
public class BootstrapFuncionarioConfig {
 @Bean
 @ConditionalOnProperty(name="app.bootstrap.enabled",havingValue="true")
 ApplicationRunner bootstrapFuncionario(FuncionarioRepository repo, PasswordEncoder encoder,
  @Value("${APP_BOOTSTRAP_EMAIL:}") String email,
  @Value("${APP_BOOTSTRAP_PASSWORD:}") String senha,
  @Value("${APP_BOOTSTRAP_CPF:}") String cpf) {
  return args -> {
   if (repo.count() != 0) return;
   if (email.isBlank() || senha.length() < 3 || !cpf.matches("\\d{11}"))
    throw new IllegalStateException("Bootstrap requer APP_BOOTSTRAP_EMAIL, APP_BOOTSTRAP_PASSWORD (3+ caracteres) e APP_BOOTSTRAP_CPF (11 digitos)");
   Funcionario f=new Funcionario();
   f.setNome("Administrador de Demonstracao"); f.setCpf(cpf);
   f.setEmail(email.trim().toLowerCase()); f.setCargo("Administrador");
   f.setSenha(encoder.encode(senha));
   repo.save(f);
  };
 }
}
