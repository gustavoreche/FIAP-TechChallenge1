package com.br.fiap.camada.interfaceUsuario;

import com.br.fiap.camada.dominio.servico.AtendimentoDTO;
import com.br.fiap.camada.dominio.servico.ContratoDTO;
import com.br.fiap.camada.dominio.servico.ContratoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.br.fiap.camada.interfaceUsuario.ContratoController.URL_CONTRATO;

@RestController
@RequestMapping(URL_CONTRATO)
public class ContratoController {

	public static final String URL_CONTRATO = "/contrato";

	@Autowired
	private ContratoService service;
	
	@PostMapping()
	public ResponseEntity<Void> registraContrato(@RequestBody @Valid ContratoDTO formulario) {
		return this.service.registraContrato(formulario);
	}

}
