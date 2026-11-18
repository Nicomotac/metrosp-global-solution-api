package br.com.fiap.metrosp.dto;
import jakarta.validation.constraints.*;
public record EstacaoRequest(
 @NotBlank @Size(max=200) String nome,
 @NotNull @Min(1) @Max(99) Integer ordem,
 @Size(max=30) String linha,
 @Pattern(regexp="[SN]", message="Use S ou N") String ativa,
 @Size(max=3) String sentido
) {}
