package com.br.fiap.camada.dominio.servico;

import com.br.fiap.camada.dominio.modelo.entidade.FiltroDeBusca;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.FilaAtendimento;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.Lead;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.LeadId;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CadastroLeadDTO(
		@NotBlank(message = "O nome nao pode ser vazio")
		@Size(min = 5, max = 50, message = "O nome deve ter no mínimo 5 letras e no máximo 50 letras")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String nome,
		
		@NotBlank(message = "O telefone nao pode ser vazio")
		@Size(min = 10, max = 11, message = "Formato de telefone inválido. Exemplo: 16911223344")
		@Positive(message = "O telefone não pode ser com valor NEGATIVO")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String telefone,
		
		@NotBlank(message = "O email nao pode ser vazio")
		@Email
		@Size(max = 50, message = "O email deve ter no máximo 50 letras")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String email,

		@NotBlank(message = "O ano nao pode ser vazio")
		@Size(min = 4, max = 4, message = "Formato de ano inválido. Exemplo: 2018")
		@Positive(message = "O ano não pode ser com valor NEGATIVO")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String ano,

		@NotBlank(message = "O modelo nao pode ser vazio")
		@Size(min = 2, max = 50, message = "O modelo deve ter no mínimo 2 letras e no máximo 50 letras")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String modelo

) {

	public Lead converteParaLead() {
		var filtroDeBusca = new FiltroDeBusca();
		filtroDeBusca.setAno(this.ano);
		filtroDeBusca.setModelo(this.modelo);
		
		var lead = new Lead();
		lead.setId(converteParaLeadId());
		lead.setTelefone(this.telefone);
		lead.setDataInsercao(LocalDateTime.now());
		lead.setFiltroDeBusca(filtroDeBusca);
		return lead;
	}
	
	public FilaAtendimento converteParaFilaDeAtendimento() {
		var filaAtendimento = new FilaAtendimento();
		filaAtendimento.setAnoFiltroDeBusca(this.ano);
		filaAtendimento.setModeloFiltroDeBusca(this.modelo);
		filaAtendimento.setId(converteParaLeadId());
		filaAtendimento.setTelefone(this.telefone);
		filaAtendimento.setDataInsercao(LocalDateTime.now());
		return filaAtendimento;
	}
	
	
	private LeadId converteParaLeadId() {
		var leadId = new LeadId();
		leadId.setNome(this.nome);
		leadId.setEmail(this.email);
		return leadId;
	}
}
