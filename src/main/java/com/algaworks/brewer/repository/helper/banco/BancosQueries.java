package com.algaworks.brewer.repository.helper.banco;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.brewer.model.Banco;
import com.algaworks.brewer.repository.filter.BancoFilter;

public interface BancosQueries {
	
	public Page<Banco> filtrar(BancoFilter filtro, Pageable pageable);

}
