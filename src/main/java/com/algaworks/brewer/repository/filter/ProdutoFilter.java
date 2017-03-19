package com.algaworks.brewer.repository.filter;

import com.algaworks.brewer.model.Representada;

public class ProdutoFilter {
	
	private Representada representada;
	private String nome;
	
	
	public Representada getRepresentada() {
		return representada;
	}
	public void setRepresentada(Representada representada) {
		this.representada = representada;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	

}
