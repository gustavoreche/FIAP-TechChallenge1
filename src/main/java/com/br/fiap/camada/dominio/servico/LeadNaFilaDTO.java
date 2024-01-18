package com.br.fiap.camada.dominio.servico;

public record LeadNaFilaDTO(
		String nome,
		String email,
		String telefone,
		String anoFiltroDeBusca,
		String modeloFiltroDeBusca
) {}
