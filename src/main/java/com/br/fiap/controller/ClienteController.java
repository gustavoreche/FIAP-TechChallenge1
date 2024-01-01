package com.br.fiap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.fiap.dto.CadastroClienteDTO;
import com.br.fiap.repository.ClienteRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
	
	@Autowired
	private ClienteRepository repository;
	
	@PostMapping()
	public ResponseEntity<Void> cadastraCliente(@RequestBody @Valid CadastroClienteDTO formulario,
			@RequestHeader LocalAcessadoEnum localAcessado) {
		if(localAcessado.equals(LocalAcessadoEnum.ESTANDE)) {
			this.repository.save(formulario.toEntity());
		} else {
			System.out.println("colocar cliente em uma fila de atendimento");
		}
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}

}
