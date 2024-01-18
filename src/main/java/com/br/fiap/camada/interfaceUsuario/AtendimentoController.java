package com.br.fiap.camada.interfaceUsuario;

import com.br.fiap.camada.dominio.servico.AtendimentoDTO;
import com.br.fiap.camada.dominio.servico.AtendimentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.br.fiap.camada.interfaceUsuario.AtendimentoController.URL_ATENDIMENTO;

@RestController
@RequestMapping(URL_ATENDIMENTO)
public class AtendimentoController {

	public static final String URL_ATENDIMENTO = "/atendimento";
	
	@Autowired
	private AtendimentoService service;
	
	@PostMapping()
	public ResponseEntity<Void> registraAtendimento(@RequestBody @Valid AtendimentoDTO formulario) {
		return this.service.registraAtendimento(formulario);
	}

}
