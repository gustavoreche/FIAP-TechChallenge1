package com.br.fiap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.fiap.dto.AtendimentoDTO;
import com.br.fiap.repository.AtendimentoRepository;
import com.br.fiap.repository.ClienteRepository;
import com.br.fiap.repository.FilaAtendimentoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vendedor")
public class VendedorController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private AtendimentoRepository atendimentoRepository;
	
	@Autowired
	private FilaAtendimentoRepository filaAtendimentoRepository;
	
	@PostMapping("/registra-atendimento")
	public ResponseEntity<Void> registraAtendimento(@RequestBody @Valid AtendimentoDTO formulario,
			@RequestHeader LocalAcessadoEnum localAcessado) {
		if(localAcessado.equals(LocalAcessadoEnum.SITE)) {
			this.clienteRepository.save(formulario.cliente().converteParaCliente());
			this.filaAtendimentoRepository.deleteById(formulario.pegaIdFilaAtendimento());
		}
		this.atendimentoRepository.save(formulario.converteParaAtendimento());
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}

}
