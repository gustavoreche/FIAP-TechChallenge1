package com.br.fiap.camada.dominio.servico;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ContratoService {

	public ResponseEntity<Void> registraContrato(ContratoDTO formulario) {
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.build();
	}

}
