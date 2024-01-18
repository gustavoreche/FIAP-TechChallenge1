package com.br.fiap.camada.dominio.modelo.entidade;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_filtro_de_busca")
@Data
public class FiltroDeBusca {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String ano;
	private String modelo;

}
