package com.br.fiap.camada.dominio.modelo.objetoDeValor;

import com.br.fiap.camada.dominio.modelo.entidade.FiltroDeBusca;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_lead")
@Data
public class Lead {
	
	@EmbeddedId
	private LeadId id;
	private String telefone;
	private LocalDateTime dataInsercao;
	@OneToOne(cascade = CascadeType.ALL)
	private FiltroDeBusca filtroDeBusca;

}
