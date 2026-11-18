package br.com.fiap.metrosp.exception;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;
import java.time.Instant;
import java.util.*;
@RestControllerAdvice
public class ApiExceptionHandler {
 private ResponseEntity<ErroApi> error(HttpStatusCode status,String mensagem,Map<String,String> campos){
  return ResponseEntity.status(status).body(new ErroApi(Instant.now(),status.value(),
   (status instanceof HttpStatus h)?h.getReasonPhrase():"Erro",mensagem,campos));
 }
 @ExceptionHandler(RecursoNaoEncontradoException.class)
 public ResponseEntity<ErroApi> missing(RecursoNaoEncontradoException ex){return error(HttpStatus.NOT_FOUND,ex.getMessage(),Map.of());}
 @ExceptionHandler(ConflitoException.class)
 public ResponseEntity<ErroApi> conflict(ConflitoException ex){return error(HttpStatus.CONFLICT,ex.getMessage(),Map.of());}
 @ExceptionHandler(DataIntegrityViolationException.class)
 public ResponseEntity<ErroApi> integrity(DataIntegrityViolationException ex){return error(HttpStatus.CONFLICT,"Operação viola restrições do banco de dados",Map.of());}
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<ErroApi> validation(MethodArgumentNotValidException ex){
  Map<String,String> campos=new LinkedHashMap<>();
  ex.getBindingResult().getFieldErrors().forEach(f->campos.putIfAbsent(f.getField(),f.getDefaultMessage()));
  return error(HttpStatus.BAD_REQUEST,"Dados inválidos",campos);
 }
 @ExceptionHandler({IllegalArgumentException.class,MethodArgumentTypeMismatchException.class})
 public ResponseEntity<ErroApi> invalid(Exception ex){return error(HttpStatus.BAD_REQUEST,"Parâmetro inválido",Map.of());}
 @ExceptionHandler(BadCredentialsException.class)
 public ResponseEntity<ErroApi> badCredentials(BadCredentialsException ex){return error(HttpStatus.UNAUTHORIZED,"E-mail ou senha inválidos",Map.of());}
 @ExceptionHandler(AccessDeniedException.class)
 public ResponseEntity<ErroApi> denied(AccessDeniedException ex){return error(HttpStatus.FORBIDDEN,"Acesso não autorizado",Map.of());}
 @ExceptionHandler(ResponseStatusException.class)
 public ResponseEntity<ErroApi> response(ResponseStatusException ex){return error(ex.getStatusCode(),ex.getReason()==null?"Falha na requisição":ex.getReason(),Map.of());}
}
