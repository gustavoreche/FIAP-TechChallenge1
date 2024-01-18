package com.br.fiap.camada.interfaceUsuario;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ClienteError {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> trataErroNaValidacao(MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult()
	    	.getAllErrors()
	    	.forEach(error -> {
	    		var fieldName = ((FieldError) error).getField();
	    		var errorMessage = error.getDefaultMessage();
	    		errors.put(fieldName, errorMessage);
	    	});
	    return errors;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public String trataErroNaoMapeado(Exception ex) {
		return ex.getMessage();
	}

}
