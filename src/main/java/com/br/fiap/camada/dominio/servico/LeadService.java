package com.br.fiap.camada.dominio.servico;

import com.br.fiap.camada.infraestrutura.FilaAtendimentoRepository;
import com.br.fiap.camada.infraestrutura.LeadRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LeadService {
	
	@Autowired
	private LeadRepository leadRepository;
	
	@Autowired
	private FilaAtendimentoRepository filaAtendimentoRepository;

	@Transactional
	public ResponseEntity<Void> cadastraLead(CadastroLeadDTO formulario) {
		this.leadRepository.save(formulario.converteParaLead());
		this.filaAtendimentoRepository.save(formulario.converteParaFilaDeAtendimento());
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}
	
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
