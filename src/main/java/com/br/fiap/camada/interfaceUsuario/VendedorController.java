package com.br.fiap.camada.interfaceUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.fiap.camada.dominio.servico.AtendimentoDTO;
import com.br.fiap.camada.dominio.servico.VendedorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vendedor")
public class VendedorController {
	
	@Autowired
	private VendedorService service;
	
	@PostMapping("/registra-atendimento")
	public ResponseEntity<Void> registraAtendimento(@RequestBody @Valid AtendimentoDTO formulario,
			@RequestHeader LocalAcessadoEnum localAcessado) {
		return this.service.registraAtendimento(formulario, localAcessado);
	}

}
