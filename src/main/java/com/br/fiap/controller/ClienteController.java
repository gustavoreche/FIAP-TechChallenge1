package com.br.fiap.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.fiap.dto.CadastroClienteDTO;
import com.br.fiap.dto.ClienteNaFilaDTO;
import com.br.fiap.repository.ClienteRepository;
import com.br.fiap.repository.FilaAtendimentoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private FilaAtendimentoRepository filaAtendimentoRepository;
	
	@PostMapping()
	public ResponseEntity<Void> cadastraCliente(@RequestBody @Valid CadastroClienteDTO formulario,
			@RequestHeader LocalAcessadoEnum localAcessado) {
		if(localAcessado.equals(LocalAcessadoEnum.ESTANDE)) {
			this.clienteRepository.save(formulario.converteParaCliente());
		} else {
			this.filaAtendimentoRepository.save(formulario.converteParaFilaDeAtendimento());
		}
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}
	
	@GetMapping("/proximo-da-fila")
	public ResponseEntity<ClienteNaFilaDTO> proximoClienteDaFila() {
		var clienteNaFila = this.filaAtendimentoRepository.findTop1ByOrderByDataInsercaoAsc();
		if(Objects.isNull(clienteNaFila)) {
			return ResponseEntity
					.noContent()
					.build();
		}
		return ResponseEntity
				.ok(clienteNaFila.converteParaClienteNaFila());
	}

}
