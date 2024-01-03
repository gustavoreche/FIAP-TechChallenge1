package com.br.fiap.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ClienteId {
	
	private String nome;
	private String email;

}
