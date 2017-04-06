package com.algaworks.brewer.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.brewer.model.Banco;
import com.algaworks.brewer.repository.Bancos;
import com.algaworks.brewer.repository.Vendas;
import com.algaworks.brewer.service.exception.DescricaoBancoJaCadastradoException;

@Service
public class CadastroBancoService {
	
	
	@Autowired
	private Bancos bancos;
	@Autowired
	private Vendas vendas;
	
	@Transactional
	public Banco salvar(Banco banco){
		Optional<Banco> bancoExistente = bancos.findByDescricao(banco.getDescricao());
		
		if(bancoExistente.isPresent()){
			throw new DescricaoBancoJaCadastradoException("Descrição do banco já cadastrado");
		}
		
		return bancos.saveAndFlush(banco);
	}
	
	@Transactional
	public void excluir(Long idBanco) {
		Banco banco = bancos.findOne(idBanco);
		if (banco == null) {
			throw new RuntimeException("Banco não existe mais.");
		}
		
		Long count = vendas.countByBanco(idBanco);
		if (count > 0) {
			throw new RuntimeException("Banco já está sendo usado");
		}
		
		bancos.delete(idBanco);
	}

}
