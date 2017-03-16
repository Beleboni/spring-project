package com.algaworks.brewer.repository.helper.comissao;

import java.util.List;

import com.algaworks.brewer.model.Comissao;
import com.algaworks.brewer.model.Venda;

public interface ComissoesQueries {

	List<Comissao> findByVenda(Venda venda);
	
}
