package br.com.fiap.metrosp.dto;
import jakarta.validation.constraints.*;
public record FuncionarioRequest(
 @NotBlank @Size(max=200) String nome,
 @NotBlank @Pattern(regexp="\\d{11}",message="CPF deve ter 11 dígitos") String cpf,
 @NotBlank @Email @Size(max=200) String email,
 @NotBlank @Size(min=3,max=72) String senha,
 @NotBlank @Size(max=60) String cargo
) {}
