package com.br.fiap.camada.dominio.servico;

public record ClienteNaFilaDTO (
		String nome,
		String email,
		String telefone,
		String anoFiltroDeBusca,
		String modeloFiltroDeBusca,
		String categoriaFiltroDeBusca
) {}
