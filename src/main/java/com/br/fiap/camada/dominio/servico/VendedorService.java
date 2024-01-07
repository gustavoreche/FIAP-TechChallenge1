package com.br.fiap.camada.dominio.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.br.fiap.camada.infraestrutura.AtendimentoRepository;
import com.br.fiap.camada.infraestrutura.ClienteRepository;
import com.br.fiap.camada.infraestrutura.FilaAtendimentoRepository;
import com.br.fiap.camada.interfaceUsuario.LocalAcessadoEnum;

@Service
public class VendedorService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private AtendimentoRepository atendimentoRepository;
	
	@Autowired
	private FilaAtendimentoRepository filaAtendimentoRepository;
	
	public ResponseEntity<Void> registraAtendimento(AtendimentoDTO formulario,
			LocalAcessadoEnum localAcessado) {
		if(localAcessado.equals(LocalAcessadoEnum.SITE)) {
			this.clienteRepository.save(formulario.cliente().converteParaCliente());
			this.filaAtendimentoRepository.deleteById(formulario.pegaIdFilaAtendimento());
		}
		this.atendimentoRepository.save(formulario.converteParaAtendimento());
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}
	
}
