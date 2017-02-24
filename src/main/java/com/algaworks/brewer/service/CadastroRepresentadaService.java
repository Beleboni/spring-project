package com.algaworks.brewer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Representada;
import com.algaworks.brewer.repository.Representadas;

@Service
public class CadastroRepresentadaService {
	
	@Autowired
	private Representadas representadas;
	
	@Transactional
	public void salvar(Representada representada){
		representadas.save(representada);
	}

}
