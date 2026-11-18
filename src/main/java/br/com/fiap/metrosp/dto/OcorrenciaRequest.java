package br.com.fiap.metrosp.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
public record OcorrenciaRequest(
 @NotBlank @Size(max=100) String tipoProblema,
 @NotBlank String mensagem,
 @NotBlank @Pattern(regexp="[A-Z]",message="Use uma letra maiúscula para status") String status,
 @NotNull LocalDateTime dataInicio,
 LocalDateTime dataFim,
 @NotNull Long estacaoId,
 @NotNull Long funcionarioId
) {}
