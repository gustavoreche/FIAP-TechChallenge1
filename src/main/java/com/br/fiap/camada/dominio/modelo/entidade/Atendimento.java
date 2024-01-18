package com.br.fiap.camada.dominio.modelo.entidade;

import com.br.fiap.camada.dominio.modelo.objetoDeValor.Lead;
import jakarta.persistence.*;
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
	private Lead lead;

}
