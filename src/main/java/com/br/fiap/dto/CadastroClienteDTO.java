package com.br.fiap.dto;

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

) {}
