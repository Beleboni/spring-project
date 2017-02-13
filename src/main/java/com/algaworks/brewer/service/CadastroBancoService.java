package com.algaworks.brewer.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.brewer.model.Banco;
import com.algaworks.brewer.repository.Bancos;
import com.algaworks.brewer.service.exception.DescricaoBancoJaCadastradoException;

@Service
public class CadastroBancoService {
	
	
	@Autowired
	private Bancos bancos;
	
	@Transactional
	public void salvar(Banco banco){
		Optional<Banco> bancoExistente = bancos.findByDescricao(banco.getDescricao());
		
		if(bancoExistente.isPresent()){
			throw new DescricaoBancoJaCadastradoException("Descrição do banco já cadastrado");
		}
		
		bancos.save(banco);
	}
	
	

}
