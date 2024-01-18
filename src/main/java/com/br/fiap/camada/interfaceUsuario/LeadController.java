package com.br.fiap.camada.interfaceUsuario;

import com.br.fiap.camada.dominio.servico.CadastroLeadDTO;
import com.br.fiap.camada.dominio.servico.LeadNaFilaDTO;
import com.br.fiap.camada.dominio.servico.LeadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.br.fiap.camada.interfaceUsuario.LeadController.URL_LEAD;

@RestController
@RequestMapping(URL_LEAD)
public class LeadController {

	public static final String URL_LEAD = "/lead";
	public static final String URL_LEAD_PROXIMO_DA_FILA = URL_LEAD.concat("/proximo-da-fila");

	@Autowired
	private LeadService service;
	
	@PostMapping()
	public ResponseEntity<Void> cadastraLead(@RequestBody @Valid CadastroLeadDTO formulario) {
		return this.service.cadastraLead(formulario);
	}
	
	@GetMapping("/proximo-da-fila")
	public ResponseEntity<LeadNaFilaDTO> proximoLeadDaFila() {
		return this.service.proximoLeadDaFila();
	}

}
