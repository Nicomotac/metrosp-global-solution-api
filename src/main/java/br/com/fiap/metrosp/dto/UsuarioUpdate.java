package br.com.fiap.metrosp.dto;
import jakarta.validation.constraints.*;
public record UsuarioUpdate(
 @NotBlank @Size(max=200) String nome,
 @NotBlank @Email @Size(max=200) String email,
 @Size(min=3,max=72) String senha
) {}
