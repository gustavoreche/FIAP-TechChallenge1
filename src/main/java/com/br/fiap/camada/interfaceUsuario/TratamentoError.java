package com.br.fiap.camada.interfaceUsuario;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class TratamentoError {
	
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

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public String trataErroNaValidacao(HttpMessageNotReadableException ex) {
		return "Erro de valor nos campos... O campo espera um valor(texto, número...) e foi passado outro valor. Segue o valor errado: " + ex.getMessage();
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public String trataErroNaoMapeado(Exception ex) {
		return ex.getMessage();
	}

}
