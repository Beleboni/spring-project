package com.algaworks.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.brewer.model.Comissao;
import com.algaworks.brewer.repository.helper.comissao.ComissoesQueries;

public interface Comissoes extends JpaRepository<Comissao, Long>, ComissoesQueries {
	
}
