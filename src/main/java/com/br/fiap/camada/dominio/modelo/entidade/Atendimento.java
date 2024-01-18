package com.br.fiap.camada.dominio.modelo.entidade;

import com.br.fiap.camada.dominio.modelo.objetoDeValor.Lead;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_atendimento")
@Data
public class Atendimento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nome; 
	@OneToOne(cascade = CascadeType.MERGE)
	private Lead cliente;

}
