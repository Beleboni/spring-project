package com.algaworks.brewer.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.brewer.model.Produto;
import com.algaworks.brewer.repository.Produtos;
import com.algaworks.brewer.service.exception.NomeProdutoJaCadastradoException;

@Service
public class CadastroProdutoService {
	
	@Autowired
	private Produtos produtos;
	
	@Transactional
	public void salvar(Produto produto){
		Optional<Produto> produtoExistente = produtos.findByNome(produto.getNome());
		
		if(produtoExistente.isPresent()){
			throw new NomeProdutoJaCadastradoException("Nome do produto já cadastrado");
		}
		
		produtos.save(produto);
	}

}
