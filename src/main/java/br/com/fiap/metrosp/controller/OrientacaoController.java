package br.com.fiap.metrosp.controller;
import br.com.fiap.metrosp.dto.*;
import br.com.fiap.metrosp.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController @RequestMapping("/api/ocorrencias")
public class OrientacaoController {
 private final OrientacaoService service;
 public OrientacaoController(OrientacaoService service){this.service=service;}
 // Requer JWT de usuario OU funcionario; a ocorrência já foi lida do Oracle pela API.
 @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
 @GetMapping("/{id}/orientacao") public OrientacaoResponse obter(@PathVariable Long id){return service.orientar(id);}
}
