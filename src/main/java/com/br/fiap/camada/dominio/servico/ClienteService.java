package com.br.fiap.camada.dominio.servico;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.br.fiap.camada.infraestrutura.ClienteRepository;
import com.br.fiap.camada.infraestrutura.FilaAtendimentoRepository;
import com.br.fiap.camada.interfaceUsuario.LocalAcessadoEnum;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private FilaAtendimentoRepository filaAtendimentoRepository;
	
	public ResponseEntity<Void> cadastraCliente(CadastroClienteDTO formulario,
			LocalAcessadoEnum localAcessado) {
		if(localAcessado.equals(LocalAcessadoEnum.ESTANDE)) {
			this.clienteRepository.save(formulario.converteParaCliente());
		} else {
			this.filaAtendimentoRepository.save(formulario.converteParaFilaDeAtendimento());
		}
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
