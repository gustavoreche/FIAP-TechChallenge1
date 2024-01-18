package com.br.fiap.camada.infraestrutura;

import com.br.fiap.camada.dominio.modelo.objetoDeValor.Lead;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.LeadId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadRepository extends JpaRepository<Lead, LeadId> {

}
