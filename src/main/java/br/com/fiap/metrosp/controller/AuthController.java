package br.com.fiap.metrosp.controller;
import br.com.fiap.metrosp.dto.*;
import br.com.fiap.metrosp.service.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final AutenticacaoService auth; private final UsuarioService usuarios;
 public AuthController(AutenticacaoService auth,UsuarioService usuarios){this.auth=auth;this.usuarios=usuarios;}
 @PostMapping("/login") public LoginResponse login(@Valid @RequestBody LoginRequest dto){return auth.entrar(dto);}
 @PostMapping("/cadastro") public ResponseEntity<UsuarioResponse> cadastro(@Valid @RequestBody UsuarioRequest dto){return ResponseEntity.status(HttpStatus.CREATED).body(usuarios.criar(dto));}
}
