package com.algaworks.brewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.brewer.model.Banco;
import com.algaworks.brewer.repository.helper.banco.BancosQueries;

public interface Bancos extends JpaRepository<Banco, Long>, BancosQueries {
	
	public List<Banco> findByDescricao(String descricao);
}
