package com.br.fiap.model;

import java.time.LocalDateTime;

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

}
