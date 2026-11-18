package br.com.fiap.metrosp.config;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
@Configuration
public class JwtConfig {
 @Bean JwtEncoder jwtEncoder(@Value("${app.jwt.secret}") String secret){
  validar(secret);
  return new NimbusJwtEncoder(new ImmutableSecret<>(secret.getBytes(StandardCharsets.UTF_8)));
 }
 @Bean JwtDecoder jwtDecoder(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.issuer}") String issuer){
  validar(secret);
  var key=new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),"HmacSHA256");
  NimbusJwtDecoder decoder=NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
  decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer));
  return decoder;
 }
 private void validar(String secret){if(secret.getBytes(StandardCharsets.UTF_8).length<32)throw new IllegalStateException("JWT_SECRET precisa conter pelo menos 32 bytes");}
}
