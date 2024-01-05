package com.br.fiap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.fiap.model.FilaAtendimento;

@Repository
public interface FilaAtendimentoRepository extends JpaRepository<FilaAtendimento, Long> {
	
	public FilaAtendimento findTop1ByOrderByDataInsercaoAsc();

}
