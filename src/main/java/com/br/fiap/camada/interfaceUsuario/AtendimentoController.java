package com.br.fiap.camada.interfaceUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.fiap.camada.dominio.servico.AtendimentoDTO;
import com.br.fiap.camada.dominio.servico.AtendimentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/atendimento")
public class AtendimentoController {
	
	@Autowired
	private AtendimentoService service;
	
	@PostMapping()
	public ResponseEntity<Void> registraAtendimento(@RequestBody @Valid AtendimentoDTO formulario,
			@RequestHeader LocalAcessadoEnum localAcessado) {
		return this.service.registraAtendimento(formulario, localAcessado);
	}

}
