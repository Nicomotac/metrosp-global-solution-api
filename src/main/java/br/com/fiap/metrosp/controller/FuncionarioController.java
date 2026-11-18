package br.com.fiap.metrosp.controller;
import br.com.fiap.metrosp.dto.*;
import br.com.fiap.metrosp.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/funcionarios")
public class FuncionarioController {
 private final FuncionarioService service;
 public FuncionarioController(FuncionarioService service){this.service=service;}
 @GetMapping public Page<FuncionarioResponse> listar(@PageableDefault(size=20,sort="id") Pageable pageable){return service.listar(pageable);}
 @GetMapping("/{id}") public FuncionarioResponse buscar(@PathVariable Long id){return service.buscar(id);}
 @PostMapping public ResponseEntity<FuncionarioResponse> criar(@Valid @RequestBody FuncionarioRequest dto){return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));}
 @PutMapping("/{id}") public FuncionarioResponse alterar(@PathVariable Long id,@Valid @RequestBody FuncionarioUpdate dto){return service.alterar(id,dto);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id){service.excluir(id);}
}
