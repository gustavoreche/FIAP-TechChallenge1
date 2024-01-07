package com.br.fiap.camada.infraestrutura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.fiap.camada.dominio.modelo.ClienteId;
import com.br.fiap.camada.dominio.modelo.FilaAtendimento;

@Repository
public interface FilaAtendimentoRepository extends JpaRepository<FilaAtendimento, ClienteId> {
	
	public FilaAtendimento findTop1ByOrderByDataInsercaoAsc();

}
