package com.br.fiap.camada.dominio.servico;

import com.br.fiap.camada.dominio.modelo.entidade.Atendimento;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.LeadId;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AtendimentoDTO (
		@NotBlank(message = "O nome nao pode ser vazio")
		@Size(min = 5, max = 50, message = "O nome deve ter no mínimo 5 letras e no máximo 50 letras")
		String nome,
		
		@NotNull
        CadastroLeadDTO cliente

) {

	public Atendimento converteParaAtendimento() {
		var atendimento = new Atendimento();
		atendimento.setNome(this.nome);
		atendimento.setCliente(this.cliente.converteParaLead());
		return atendimento;
	}

	public LeadId pegaIdFilaAtendimento() {
		var clienteId = new LeadId();
		clienteId.setNome(this.cliente.nome());
		clienteId.setEmail(this.cliente.email());
		return clienteId;
	}
}
