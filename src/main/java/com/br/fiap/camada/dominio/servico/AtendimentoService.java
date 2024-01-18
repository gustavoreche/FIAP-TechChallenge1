package com.br.fiap.camada.dominio.servico;

import com.br.fiap.camada.dominio.modelo.objetoDeValor.Lead;
import com.br.fiap.camada.infraestrutura.AtendimentoRepository;
import com.br.fiap.camada.infraestrutura.FilaAtendimentoRepository;
import com.br.fiap.camada.infraestrutura.LeadRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AtendimentoService {
	
	@Autowired
	private AtendimentoRepository atendimentoRepository;
	
	@Autowired
	private FilaAtendimentoRepository filaAtendimentoRepository;

	@Autowired
	private LeadRepository leadRepository;

	@Transactional
	public ResponseEntity<Void> registraAtendimento(AtendimentoDTO formulario) {
		Optional<Lead> lead = this.leadRepository.findById(formulario.pegaLeadId());
		if(lead.isEmpty()) {
			throw new RuntimeException("Não pode registrar um atendimento sem capturar um LEAD");
		}
		this.filaAtendimentoRepository.deleteById(formulario.pegaLeadId());
		this.atendimentoRepository.save(formulario.converteParaAtendimento());
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}
	
}
