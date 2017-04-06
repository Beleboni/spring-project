package com.algaworks.brewer.repository.helper.banco;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Banco;
import com.algaworks.brewer.repository.filter.BancoFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

public class BancosImpl implements BancosQueries{

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Banco> filtrar(BancoFilter filtro, Pageable pageable) {
		Criteria criteria  = manager.unwrap(Session.class).createCriteria(Banco.class);
		
		paginacaoUtil.preparar(criteria, pageable);
		adicionarFiltro(filtro, criteria);
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	private Long total(BancoFilter filtro){
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Banco.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(BancoFilter filtro, Criteria criteria) {
		if(filtro != null){	
			if(!StringUtils.isEmpty(filtro.getDescricao())){
				criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
			}
		}		
	}

	@Override
	public List<Banco> bancosAtivo(Long codigo) {
		
		return manager.createQuery("select b from Banco b where b.ativo is true and b.codEmpresa = ?1", Banco.class)
				.setParameter(1, codigo)
				.getResultList();
	}

}
