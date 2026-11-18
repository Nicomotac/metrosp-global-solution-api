package br.com.fiap.metrosp.controller;

import br.com.fiap.metrosp.dto.LoginRequest;
import br.com.fiap.metrosp.dto.LoginResponse;
import br.com.fiap.metrosp.service.AutenticacaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Alias JSON para aplicativos Mobile que utilizavam POST /login.
 * A API principal e a autenticação permanecem em /api/auth/login.
 * NÃO usa formulário HTML nem sessão como o projeto web Clyvo.
 */
@RestController
public class LoginCompatController {
    private final AutenticacaoService autenticacaoService;

    public LoginCompatController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest dados) {
        return autenticacaoService.entrar(dados);
    }
}
