package com.br.fiap.camada.dominio.servico;

import com.br.fiap.camada.dominio.modelo.entidade.Atendimento;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.Lead;
import com.br.fiap.camada.infraestrutura.AtendimentoRepository;
import com.br.fiap.camada.infraestrutura.FilaAtendimentoRepository;
import com.br.fiap.camada.infraestrutura.LeadRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
	public ResponseEntity<Long> registraAtendimento(AtendimentoDTO formulario) {
		Optional<Lead> lead = this.leadRepository.findById(formulario.pegaLeadId());
		if(lead.isEmpty()) {
			throw new RuntimeException("Não pode registrar um atendimento sem capturar um LEAD");
		}
		this.filaAtendimentoRepository.deleteById(formulario.pegaLeadId());
		var atendimento = this.atendimentoRepository.save(formulario.converteParaAtendimento());
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(atendimento.getId());
	}

	@Transactional
	public ResponseEntity<String> enviaProposta(Long atendimentoId,
											  BigDecimal valorDaProposta) {
		Optional<Atendimento> atendimento = this.atendimentoRepository.findById(atendimentoId);
		if(atendimento.isEmpty()) {
			throw new RuntimeException("Não pode enviar uma proposta sem ter iniciado um ATENDIMENTO");
		}
		var atendimentoEntidade = atendimento.get();
		atendimentoEntidade.setValorDaProposta(valorDaProposta);
		this.atendimentoRepository.save(atendimentoEntidade);
		this.simulacaoDeEnvioDeEmail(atendimentoEntidade.getLead().getId().getEmail());
		return ResponseEntity
				.status(HttpStatus.ACCEPTED)
				.body("Um e-mail será enviado ao cliente com o valor da PROPOSTA");
	}

	private void simulacaoDeEnvioDeEmail(String email) {
		System.out.println("""
				.....................................................................
				.....................................................................
						
				Simulando o envio de email para: %s
						
				.....................................................................
				.....................................................................
				""".formatted(email));
	}

}
