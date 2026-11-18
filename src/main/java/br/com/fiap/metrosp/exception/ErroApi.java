package br.com.fiap.metrosp.exception;
import java.time.Instant;
import java.util.Map;
public record ErroApi(Instant instante,int status,String erro,String mensagem,Map<String,String> campos) {}
