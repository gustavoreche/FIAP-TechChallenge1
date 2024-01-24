package com.br.fiap.camada.dominio.servico;

import com.br.fiap.camada.infraestrutura.AtendimentoRepository;
import com.br.fiap.camada.infraestrutura.ContratoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ContratoService {

	@Autowired
	private AtendimentoRepository atendimentoRepository;

	@Autowired
	private ContratoRepository contratoRepository;

	@Transactional
	public ResponseEntity<Void> registraContrato(ContratoDTO formulario) {
		var proposta = this.atendimentoRepository
				.findTop1ByLead_id_nomeAndLead_id_emailAndStatusPropostaIsNullOrderByIdDesc(formulario.nome(), formulario.email());
		if(Objects.isNull(proposta)) {
			throw new RuntimeException("Não pode registrar um CONTRATO sem iniciar um ATENDIMENTO");
		}
		var atendimento = this.atendimentoRepository.findById(proposta.getId()).get();
		atendimento.setStatusProposta(StatusPropostaEnum.ACEITADA.name());
		this.contratoRepository.save(formulario.converteParaContrato(atendimento));
		this.atendimentoRepository.save(atendimento);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}

}
