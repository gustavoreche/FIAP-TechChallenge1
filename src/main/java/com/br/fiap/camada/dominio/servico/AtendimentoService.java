package com.br.fiap.camada.dominio.servico;

import com.br.fiap.camada.infraestrutura.AtendimentoRepository;
import com.br.fiap.camada.infraestrutura.FilaAtendimentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AtendimentoService {
	
	@Autowired
	private AtendimentoRepository atendimentoRepository;
	
	@Autowired
	private FilaAtendimentoRepository filaAtendimentoRepository;

	@Transactional
	public ResponseEntity<Void> registraAtendimento(AtendimentoDTO formulario) {
		this.filaAtendimentoRepository.deleteById(formulario.pegaIdFilaAtendimento());
		this.atendimentoRepository.save(formulario.converteParaAtendimento());
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}
	
}
