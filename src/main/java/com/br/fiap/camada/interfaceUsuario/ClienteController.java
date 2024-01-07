package com.br.fiap.camada.interfaceUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.fiap.camada.dominio.servico.CadastroClienteDTO;
import com.br.fiap.camada.dominio.servico.ClienteNaFilaDTO;
import com.br.fiap.camada.dominio.servico.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
	
	@Autowired
	private ClienteService service;
	
	@PostMapping()
	public ResponseEntity<Void> cadastraCliente(@RequestBody @Valid CadastroClienteDTO formulario,
			@RequestHeader LocalAcessadoEnum localAcessado) {
		return this.service.cadastraCliente(formulario, localAcessado);
	}
	
	@GetMapping("/proximo-da-fila")
	public ResponseEntity<ClienteNaFilaDTO> proximoClienteDaFila() {
		return this.service.proximoClienteDaFila();
	}

}
