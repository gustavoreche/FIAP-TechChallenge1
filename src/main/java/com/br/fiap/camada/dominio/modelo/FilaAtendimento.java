package com.br.fiap.camada.dominio.modelo;

import java.time.LocalDateTime;

import com.br.fiap.camada.dominio.servico.ClienteNaFilaDTO;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_fila_atendimento")
@Data
public class FilaAtendimento {
	
	@EmbeddedId
	private ClienteId id; 
	private String telefone;
	private String anoFiltroDeBusca;
	private String modeloFiltroDeBusca;
	private String categoriaFiltroDeBusca;
	private LocalDateTime dataInsercao;
	
	public ClienteNaFilaDTO converteParaClienteNaFila() {
		return new ClienteNaFilaDTO(
				this.id.getNome(),
				this.id.getEmail(),
				this.telefone,
				this.anoFiltroDeBusca,
				this.modeloFiltroDeBusca,
				this.categoriaFiltroDeBusca
			);
	}

}
