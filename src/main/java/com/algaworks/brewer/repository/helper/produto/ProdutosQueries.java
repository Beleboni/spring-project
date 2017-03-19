package com.algaworks.brewer.repository.helper.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.brewer.model.Produto;
import com.algaworks.brewer.repository.filter.ProdutoFilter;

public interface ProdutosQueries {
	
	public Page<Produto> filtrar(ProdutoFilter filtro, Pageable pageable);

}
