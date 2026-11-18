package br.com.fiap.metrosp.controller;
import br.com.fiap.metrosp.dto.*;
import br.com.fiap.metrosp.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/estacoes")
public class EstacaoController {
 private final EstacaoService service;
 public EstacaoController(EstacaoService service){this.service=service;}
 @GetMapping public Page<EstacaoResponse> listar(@PageableDefault(size=20,sort="id") Pageable pageable){return service.listar(pageable);}
 @GetMapping("/{id}") public EstacaoResponse buscar(@PathVariable Long id){return service.buscar(id);}
 @PostMapping public ResponseEntity<EstacaoResponse> criar(@Valid @RequestBody EstacaoRequest dto){return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));}
 @PutMapping("/{id}") public EstacaoResponse alterar(@PathVariable Long id,@Valid @RequestBody EstacaoRequest dto){return service.alterar(id,dto);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id){service.excluir(id);}
}
