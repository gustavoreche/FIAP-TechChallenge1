package com.br.fiap.camada.dominio.servico;

import com.br.fiap.camada.infraestrutura.AtendimentoRepository;
import com.br.fiap.camada.infraestrutura.FilaAtendimentoRepository;
import com.br.fiap.camada.infraestrutura.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AtendimentoService {
	
	@Autowired
	private LeadRepository clienteRepository;
	
	@Autowired
	private AtendimentoRepository atendimentoRepository;
	
	@Autowired
	private FilaAtendimentoRepository filaAtendimentoRepository;
	
	public ResponseEntity<Void> registraAtendimento(AtendimentoDTO formulario) {
//		if(localAcessado.equals(LocalAcessadoEnum.SITE)) {
//			this.clienteRepository.save(formulario.cliente().converteParaLead());
//			this.filaAtendimentoRepository.deleteById(formulario.pegaIdFilaAtendimento());
//		}
		this.atendimentoRepository.save(formulario.converteParaAtendimento());
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}
	
}
