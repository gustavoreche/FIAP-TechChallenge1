package com.br.fiap.camada.infraestrutura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.fiap.camada.dominio.modelo.objetoDeValor.LeadId;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.FilaAtendimento;

@Repository
public interface FilaAtendimentoRepository extends JpaRepository<FilaAtendimento, LeadId> {
	
	public FilaAtendimento findTop1ByOrderByDataInsercaoAsc();

}
