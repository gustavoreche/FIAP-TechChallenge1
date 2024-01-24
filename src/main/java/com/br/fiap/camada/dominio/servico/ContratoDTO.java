package com.br.fiap.camada.dominio.servico;

import com.br.fiap.camada.dominio.modelo.entidade.Atendimento;
import com.br.fiap.camada.dominio.modelo.entidade.Contrato;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ContratoDTO(

		@NotBlank(message = "O nome nao pode ser vazio")
		@Size(min = 5, max = 50, message = "O nome deve ter no mínimo 5 letras e no máximo 50 letras")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String nome,
		@NotBlank(message = "O email nao pode ser vazio")
		@Email
		@Size(max = 50, message = "O email deve ter no máximo 50 letras")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String email,
		@NotBlank
		@CPF(message = "não esta no formato de CPF esperado")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String cpf,
		@NotNull
		@Positive(message = "O diaDataVencimento não pode ser com valor NEGATIVO")
		@Min(value = 1, message = "O diaDataVencimento tem que ser igual ou maior que 1")
		@Max(value = 31, message = "O diaDataVencimento tem que ser igual ou menor que 31")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String diaDataVencimento,
		@NotNull
		@Pattern(regexp = "24|36", message = "A quantidadeDeParcelas tem que ser 24 ou 36")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String quantidadeDeParcelas,
		@NotNull
		@Positive(message = "O valorDaProposta não pode ser com valor NEGATIVO")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String valorDaParcela
) {
	public Contrato converteParaContrato(Atendimento atendimento) {
		var contrato = new Contrato();
		contrato.setAtendimento(atendimento);
		contrato.setDataInsercao(LocalDateTime.now());
		contrato.setQuantidadeParcelas(Integer.parseInt(this.quantidadeDeParcelas));
		contrato.setValorPorParcela(new BigDecimal(this.valorDaParcela));
		return contrato;
	}
}
