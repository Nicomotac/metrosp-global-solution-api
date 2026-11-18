package br.com.fiap.metrosp.controller;
import br.com.fiap.metrosp.dto.*;
import br.com.fiap.metrosp.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/ocorrencias")
public class OcorrenciaController {
 private final OcorrenciaService service;
 public OcorrenciaController(OcorrenciaService service){this.service=service;}
 @GetMapping public Page<OcorrenciaResponse> listar(@RequestParam(required=false) Long estacaoId,
   @PageableDefault(size=20,sort="dataInicio",direction=Sort.Direction.DESC) Pageable pageable){return service.listar(estacaoId,pageable);}
 @GetMapping("/{id}") public OcorrenciaResponse buscar(@PathVariable Long id){return service.buscar(id);}
 @PostMapping public ResponseEntity<OcorrenciaResponse> criar(@Valid @RequestBody OcorrenciaRequest dto){return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));}
 @PutMapping("/{id}") public OcorrenciaResponse alterar(@PathVariable Long id,@Valid @RequestBody OcorrenciaRequest dto){return service.alterar(id,dto);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id){service.excluir(id);}
}
