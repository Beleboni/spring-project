package com.algaworks.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.brewer.model.Produto;
import com.algaworks.brewer.repository.helper.pedido.ProdutosQueries;

public interface Produtos extends JpaRepository<Produto, Long>, ProdutosQueries {
	

}
