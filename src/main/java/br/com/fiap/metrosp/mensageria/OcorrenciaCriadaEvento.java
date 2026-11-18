package br.com.fiap.metrosp.mensageria;
/** Evento de dominio gerado apos persistir uma ocorrencia. */
public record OcorrenciaCriadaEvento(Long ocorrenciaId, Long estacaoId, String tipoProblema) {}
