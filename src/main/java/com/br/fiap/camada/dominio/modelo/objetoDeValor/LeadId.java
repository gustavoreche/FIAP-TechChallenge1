package com.br.fiap.camada.dominio.modelo.objetoDeValor;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class LeadId {
	
	private String nome;
	private String email;

}
