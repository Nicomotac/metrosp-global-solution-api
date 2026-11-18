package br.com.fiap.metrosp.dto;
import jakarta.validation.constraints.*;
public record LoginRequest(@NotBlank @Email String email,@NotBlank String senha) {}
