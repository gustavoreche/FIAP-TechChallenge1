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
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
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

	public ResponseEntity<Void> recusaProposta(Long atendimentoId) {
		Optional<Atendimento> atendimento = this.atendimentoRepository.findById(atendimentoId);
		if(atendimento.isEmpty()) {
			throw new RuntimeException("Não pode recusar uma proposta sem ter iniciado um ATENDIMENTO");
		}
		var atendimentoEntidade = atendimento.get();
		atendimentoEntidade.setStatusProposta(StatusPropostaEnum.RECUSADA.name());
		this.atendimentoRepository.save(atendimentoEntidade);
		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
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

	public ResponseEntity<InformaPropostaDTO> pegaProposta(String leadNome,
													String leadEmail) {
		var proposta = this.atendimentoRepository
				.findTop1ByLead_id_nomeAndLead_id_emailAndStatusPropostaIsNullOrderByIdDesc(leadNome, leadEmail);
		if(Objects.isNull(proposta)) {
			return ResponseEntity
					.noContent()
					.build();
		}

		BigDecimal valorDaParcelaEm24Vezes = proposta.getValorDaProposta().divide(new BigDecimal(24), RoundingMode.UP);
		BigDecimal valorDaParcelaEm36Vezes = proposta.getValorDaProposta().divide(new BigDecimal(36), RoundingMode.UP);
		var parcelaEm24 = new ParcelasDTO(24, valorDaParcelaEm24Vezes);
		var parcelaEm36 = new ParcelasDTO(36, valorDaParcelaEm36Vezes);
		var parcelas = List.of(parcelaEm24, parcelaEm36);
		return ResponseEntity
				.ok(
						new InformaPropostaDTO(
								proposta.getId(),
								proposta.getValorDaProposta(),
								parcelas
						)
				);
	}
}
