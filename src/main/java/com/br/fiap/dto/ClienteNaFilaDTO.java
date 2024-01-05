package com.br.fiap.dto;

public record ClienteNaFilaDTO (
		String nome,
		String email,
		String telefone,
		String anoFiltroDeBusca,
		String modeloFiltroDeBusca,
		String categoriaFiltroDeBusca
) {}
