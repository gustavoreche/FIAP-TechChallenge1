package com.br.fiap.dto;

import com.br.fiap.model.Cliente;
import com.br.fiap.model.FiltroDeBusca;

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

	public Cliente toEntity() {
		var filtroDeBusca = new FiltroDeBusca();
		filtroDeBusca.setAno(this.ano);
		filtroDeBusca.setModelo(this.modelo);
		filtroDeBusca.setCategoria(this.categoria);		
		
		var cliente = new Cliente();
		cliente.setNome(this.nome);
		cliente.setTelefone(this.telefone);
		cliente.setEmail(this.email);
		cliente.setFiltroDeBusca(filtroDeBusca);
		return cliente;
	}
}
