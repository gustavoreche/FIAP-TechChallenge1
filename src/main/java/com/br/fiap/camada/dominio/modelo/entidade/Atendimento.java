package com.br.fiap.camada.dominio.modelo.entidade;

import com.br.fiap.camada.dominio.modelo.objetoDeValor.Lead;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_atendimento")
@Data
public class Atendimento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nomeVendedor;
	@ManyToOne
	private Lead lead;
	private BigDecimal valorDaProposta;

}
