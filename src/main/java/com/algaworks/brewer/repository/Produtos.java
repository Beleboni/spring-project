package com.algaworks.brewer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.brewer.model.Produto;
import com.algaworks.brewer.repository.helper.produto.ProdutosQueries;

public interface Produtos extends JpaRepository<Produto, Long>, ProdutosQueries {

	public Optional<Produto> findByNome(String nome);

}
