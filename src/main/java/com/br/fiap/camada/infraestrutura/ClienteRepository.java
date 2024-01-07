package com.br.fiap.camada.infraestrutura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.fiap.camada.dominio.modelo.objetoDeValor.Cliente;
import com.br.fiap.camada.dominio.modelo.objetoDeValor.ClienteId;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, ClienteId> {

}
