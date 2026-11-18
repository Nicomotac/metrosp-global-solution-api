package br.com.fiap.metrosp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
@EnableMethodSecurity @SpringBootApplication
public class MetroSpApplication {
  public static void main(String[] args) { SpringApplication.run(MetroSpApplication.class,args); }
}
