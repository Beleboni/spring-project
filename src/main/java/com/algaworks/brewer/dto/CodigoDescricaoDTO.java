package com.algaworks.brewer.dto;

public class CodigoDescricaoDTO implements CodigoDescricao {

	private Long codigo;
	private String descricao;

	public CodigoDescricaoDTO(Long codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public Long getCodigo() {
		return this.codigo;
	}

	@Override
	public String getDescricao() {
		return this.descricao;
	}

}
