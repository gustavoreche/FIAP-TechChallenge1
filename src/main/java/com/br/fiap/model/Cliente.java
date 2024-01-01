package com.br.fiap.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_cliente")
@Data
public class Cliente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	private String nome;
	private String telefone;
	private String email;
	@OneToOne(cascade = CascadeType.ALL)
	private FiltroDeBusca filtroDeBusca;

}
