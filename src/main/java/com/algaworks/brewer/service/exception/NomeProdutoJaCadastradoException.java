package com.algaworks.brewer.service.exception;

public class NomeProdutoJaCadastradoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NomeProdutoJaCadastradoException(String message){
		super(message);
	}
	
}
