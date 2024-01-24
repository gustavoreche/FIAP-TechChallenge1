package com.br.fiap.camada.dominio.servico;

import com.br.fiap.camada.dominio.modelo.entidade.Atendimento;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.LeadId;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AtendimentoDTO (
		@NotBlank(message = "O nomeVendedor nao pode ser vazio")
		@Size(min = 5, max = 50, message = "O nomeVendedor deve ter no mínimo 5 letras e no máximo 50 letras")
		String nomeVendedor,
		
		@NotNull
        CadastroLeadDTO lead

) {

	public Atendimento converteParaAtendimento() {
		var atendimento = new Atendimento();
		atendimento.setNomeVendedor(this.nomeVendedor);
		atendimento.setLead(this.lead.converteParaLead());
		return atendimento;
	}

	public LeadId pegaLeadId() {
		var leadId = new LeadId();
		leadId.setNome(this.lead.nome());
		leadId.setEmail(this.lead.email());
		return leadId;
	}
}
