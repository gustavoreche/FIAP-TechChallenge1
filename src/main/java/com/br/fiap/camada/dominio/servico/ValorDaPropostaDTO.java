package com.br.fiap.camada.dominio.servico;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ValorDaPropostaDTO(

		@NotNull
		@Positive(message = "O valorDaProposta não pode ser com valor NEGATIVO")
		BigDecimal valorDaProposta

) {}
