package com.br.fiap.camada.dominio.modelo.objetoDeValor;

import com.br.fiap.camada.dominio.modelo.entidade.FiltroDeBusca;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_cliente")
@Data
public class Cliente {
	
	@EmbeddedId
	private ClienteId id; 
	private String telefone;
	@OneToOne(cascade = CascadeType.ALL)
	private FiltroDeBusca filtroDeBusca;

}