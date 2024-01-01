package com.br.fiap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.fiap.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
