package com.br.fiap.camada.dominio.modelo.entidade;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_contrato")
@Data
public class Contrato {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@OneToOne
	private Atendimento atendimento;
	private int quantidadeParcelas;
	private BigDecimal valorPorParcela;
	private LocalDateTime dataInsercao;

}
