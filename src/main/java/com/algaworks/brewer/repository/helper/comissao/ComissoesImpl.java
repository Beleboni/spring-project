package com.algaworks.brewer.repository.helper.comissao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Comissao;
import com.algaworks.brewer.model.Venda;

public class ComissoesImpl implements ComissoesQueries{

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	@Transactional(readOnly = true)
	public List<Comissao> findByVenda(Venda venda) {
		final String sql = "select distinct c from Comissao c where c.venda = ?1";
		TypedQuery<Comissao> query = manager.createQuery(sql, Comissao.class);
		query.setParameter(1, venda);
		return query.getResultList();
	}

}
