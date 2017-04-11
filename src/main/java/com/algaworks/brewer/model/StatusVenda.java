package com.algaworks.brewer.model;

public enum StatusVenda {

	ORCAMENTO("Orçamento"), 
	EMITIDA("Emitida"), 
	CANCELADA("Cancelada"),
	ENTREGUE_PARCIALMENTE("Entregue parcialmente"),
	CONCLUIDO("Concluído");
	
	private String descricao;

	StatusVenda(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
