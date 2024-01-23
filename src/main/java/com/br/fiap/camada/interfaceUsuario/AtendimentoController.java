package com.br.fiap.camada.interfaceUsuario;

import com.br.fiap.camada.dominio.servico.AtendimentoDTO;
import com.br.fiap.camada.dominio.servico.AtendimentoService;
import com.br.fiap.camada.dominio.servico.ValorDaPropostaDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.br.fiap.camada.interfaceUsuario.AtendimentoController.URL_ATENDIMENTO;

@RestController
@RequestMapping(URL_ATENDIMENTO)
public class AtendimentoController {

	public static final String URL_ATENDIMENTO = "/atendimento";
	public static final String URL_VALOR_PROPOSTA = URL_ATENDIMENTO.concat("/envia-proposta/{atendimentoId}");

	@Autowired
	private AtendimentoService service;
	
	@PostMapping()
	public ResponseEntity<Long> registraAtendimento(@RequestBody @Valid AtendimentoDTO formulario) {
		return this.service.registraAtendimento(formulario);
	}

	@PutMapping("/envia-proposta/{atendimentoId}")
	public ResponseEntity<String> enviaProposta(@PathVariable("atendimentoId") Long atendimentoId,
											  @RequestBody @Valid ValorDaPropostaDTO valorDaPropostaDTO) {
		return this.service.enviaProposta(atendimentoId, valorDaPropostaDTO.valorDaProposta());
	}

}
