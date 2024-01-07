package com.br.fiap.camada.dominio.modelo.objetoDeValor;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ClienteId {
	
	private String nome;
	private String email;

}