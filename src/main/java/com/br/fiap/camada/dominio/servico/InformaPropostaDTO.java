package com.br.fiap.camada.dominio.servico;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record InformaPropostaDTO(

		Long idAtendimento,
		BigDecimal valorDaProposta,
		List<ParcelasDTO> parcelas

) {}
