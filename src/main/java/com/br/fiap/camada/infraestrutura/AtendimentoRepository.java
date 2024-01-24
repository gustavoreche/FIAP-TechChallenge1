package com.br.fiap.camada.infraestrutura;

import com.br.fiap.camada.dominio.modelo.entidade.Atendimento;
import com.br.fiap.camada.dominio.servico.ProjectValorEIdDaProposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    ProjectValorEIdDaProposta findTop1ByLead_id_nomeAndLead_id_emailOrderByIdDesc(String leadNome, String leadEmail);
}
