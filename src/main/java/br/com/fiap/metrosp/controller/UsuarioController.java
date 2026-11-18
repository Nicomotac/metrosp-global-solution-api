package br.com.fiap.metrosp.controller;
import br.com.fiap.metrosp.dto.*;
import br.com.fiap.metrosp.service.*;
import br.com.fiap.metrosp.security.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/usuarios")
public class UsuarioController {
 private final UsuarioService service;private final AccountAuthorization authorization;
 public UsuarioController(UsuarioService service,AccountAuthorization authorization){this.service=service;this.authorization=authorization;}
 @GetMapping @PreAuthorize("hasRole('ADMIN')") public Page<UsuarioResponse> listar(@PageableDefault(size=20,sort="id") Pageable pageable){return service.listar(pageable);}
 @GetMapping("/{id}") public UsuarioResponse buscar(@PathVariable Long id,Authentication auth){authorization.somenteProprioOuFuncionario(id,auth);return service.buscar(id);}
 @PostMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest dto){return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));}
 @PutMapping("/{id}") public UsuarioResponse alterar(@PathVariable Long id,@Valid @RequestBody UsuarioUpdate dto,Authentication auth){authorization.somenteProprioOuFuncionario(id,auth);return service.alterar(id,dto);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id,Authentication auth){authorization.somenteProprioOuFuncionario(id,auth);service.excluir(id);}
}
