package com.br.fiap.camada.dominio.servico;

import java.time.LocalDateTime;

import com.br.fiap.camada.dominio.modelo.entidade.FiltroDeBusca;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.Cliente;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.ClienteId;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.FilaAtendimento;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CadastroClienteDTO (
		@NotBlank(message = "O nome nao pode ser vazio")
		@Size(min = 5, max = 50, message = "O nome deve ter no mínimo 5 letras e no máximo 50 letras")
		String nome,
		
		@NotBlank(message = "O telefone nao pode ser vazio")
		@Size(min = 10, max = 11, message = "Formato de telefone inválido. Exemplo: 16911223344")
		@Positive(message = "O telefone só deve conter números")
		String telefone,
		
		@NotBlank(message = "O email nao pode ser vazio")
		@Email
		@Size(max = 50, message = "O email deve ter no máximo 50 letras")
		String email,
		
		String ano,
		
		String modelo,
		
		String categoria

) {

	public Cliente converteParaCliente() {
		var filtroDeBusca = new FiltroDeBusca();
		filtroDeBusca.setAno(this.ano);
		filtroDeBusca.setModelo(this.modelo);
		filtroDeBusca.setCategoria(this.categoria);		
		
		var cliente = new Cliente();
		cliente.setId(converteParaClienteId());
		cliente.setTelefone(this.telefone);
		cliente.setFiltroDeBusca(filtroDeBusca);
		return cliente;
	}
	
	public FilaAtendimento converteParaFilaDeAtendimento() {
		var filaAtendimento = new FilaAtendimento();
		filaAtendimento.setAnoFiltroDeBusca(this.ano);
		filaAtendimento.setModeloFiltroDeBusca(this.modelo);
		filaAtendimento.setCategoriaFiltroDeBusca(this.categoria);		
		filaAtendimento.setId(converteParaClienteId());
		filaAtendimento.setTelefone(this.telefone);
		filaAtendimento.setDataInsercao(LocalDateTime.now());
		return filaAtendimento;
	}
	
	
	private ClienteId converteParaClienteId() {
		var clienteId = new ClienteId();
		clienteId.setNome(this.nome);
		clienteId.setEmail(this.email);
		return clienteId;
	}
}
