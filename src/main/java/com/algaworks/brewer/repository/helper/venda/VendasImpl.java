package com.algaworks.brewer.repository.helper.venda;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.dto.VendaMes;
import com.algaworks.brewer.dto.VendaOrigem;
import com.algaworks.brewer.model.StatusVenda;
import com.algaworks.brewer.model.Usuario;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.Vendas;
import com.algaworks.brewer.repository.filter.VendaFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

public class VendasImpl implements VendasQueries {

	public static final String CLAUSE_FROM_AND_WHERE_FILTRAR = "from Venda v left join v.cliente c left join v.usuario u "
			+ "where (v.dataCriacao between ?1 and ?2) and (v.status = ?3 or ?3 is null) "
			+ "and (c.nome like concat('%', ?4, '%') or ?4 is null) "
			+ "and (v.usuario = ?5 or ?5 is null))";
	
//	public static final String CLAUSE_FROM_AND_WHERE_FILTRAR = "from Venda v left join v.cliente c left join v.usuario u "
//			+ "where (v.dataCriacao between ?1 and ?2) and (v.status = ?3 or ?3 is null) "
//			+ "and (c.nome like concat('%', ?4, '%') or ?4 is null) "
//			+ "and (v.usuario = ?5 or ?5 is null) and (v.empresa.codigo = ?6)";

	@PersistenceContext
	private EntityManager manager;

	@Autowired
	private PaginacaoUtil paginacaoUtil;

	@Autowired
	private Vendas vendas;

	@Override
	@Transactional(readOnly = true)
	public Page<Venda> filtrar(VendaFilter filtro, Usuario usuario, Pageable pageable) {
		Usuario user = usuario.isAdministrador() ? null : usuario;
		return vendas.filtrar(filtro.getDesdeWithTime(), filtro.getAteWithTime(), filtro.getStatus(), 
				filtro.getNomeCliente(), user, pageable);
	}
	
//	@Override
//	@Transactional(readOnly = true)
//	public Page<Venda> filtrar(VendaFilter filtro, Usuario usuario, Pageable pageable) {
//		Usuario user = usuario.isAdministrador() ? null : usuario;
//		return vendas.filtrar(filtro.getDesdeWithTime(), filtro.getAteWithTime(), filtro.getStatus(), 
//				filtro.getNomeCliente(), user, usuario.getUsuario().getCodigo(), pageable);
//	}

	@Transactional(readOnly = true)
	@Override
	public Venda buscarComItens(Long codigo) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(
				Venda.class);
		criteria.createAlias("itens", "i", JoinType.LEFT_OUTER_JOIN);
		// criteria.createAlias("comissoes", "c", JoinType.LEFT_OUTER_JOIN);
		// Remover linhas comentadas
		criteria.add(Restrictions.eq("codigo", codigo));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (Venda) criteria.uniqueResult();
	}

	@Override
	public BigDecimal valorTotalNoAno() {
		Optional<BigDecimal> optional = Optional
				.ofNullable(manager
						.createQuery(
								"select sum(valorTotal) from Venda where year(dataCriacao) = :ano and status = :status",
								BigDecimal.class)
						.setParameter("ano", Year.now().getValue())
						.setParameter("status", StatusVenda.EMITIDA)
						.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}

	@Override
	public BigDecimal valorTotalNoMes() {
		Optional<BigDecimal> optional = Optional
				.ofNullable(manager
						.createQuery(
								"select sum(valorTotal) from Venda where month(dataCriacao) = :mes and status = :status",
								BigDecimal.class)
						.setParameter("mes", MonthDay.now().getMonthValue())
						.setParameter("status", StatusVenda.EMITIDA)
						.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}

	@Override
	public Long countOrcamento() {
		return manager
				.createQuery(
						"select count(codigo) from Venda where status = :status",
						Long.class)
				.setParameter("status", StatusVenda.ORCAMENTO)
				.getSingleResult();
	}

	@Override
	public BigDecimal valorTicketMedioNoAno() {
		Optional<BigDecimal> optional = Optional
				.ofNullable(manager
						.createQuery(
								"select sum(valorTotal)/count(*) from Venda where year(dataCriacao) = :ano and status = :status",
								BigDecimal.class)
						.setParameter("ano", Year.now().getValue())
						.setParameter("status", StatusVenda.EMITIDA)
						.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VendaMes> totalPorMes(Long codigo) {
		Query query = manager.createNamedQuery("Vendas.totalPorMes");
		query.setParameter(1, codigo);

		List<VendaMes> vendasMes = query.getResultList();

		LocalDate hoje = LocalDate.now();
		for (int i = 1; i <= 6; i++) {
			String mesIdeal = String.format("%d/%02d", hoje.getYear(),
					hoje.getMonthValue());

			boolean possuiMes = vendasMes.stream()
					.filter(v -> v.getMes().equals(mesIdeal)).findAny()
					.isPresent();
			if (!possuiMes) {
				vendasMes.add(i - 1, new VendaMes(mesIdeal, 0));
			}

			hoje = hoje.minusMonths(1);
		}

		return vendasMes;
	}

	@Override
	public List<VendaOrigem> totalPorOrigem() {
		List<VendaOrigem> vendasNacionalidade = manager.createNamedQuery(
				"Vendas.porOrigem", VendaOrigem.class).getResultList();

		LocalDate now = LocalDate.now();
		for (int i = 1; i <= 6; i++) {
			String mesIdeal = String.format("%d/%02d", now.getYear(), now
					.getMonth().getValue());

			boolean possuiMes = vendasNacionalidade.stream()
					.filter(v -> v.getMes().equals(mesIdeal)).findAny()
					.isPresent();
			if (!possuiMes) {
				vendasNacionalidade.add(i - 1, new VendaOrigem(mesIdeal, 0, 0));
			}

			now = now.minusMonths(1);
		}

		return vendasNacionalidade;
	}

	@Override
	public Long countByBanco(Long idBanco) {
		// return
		// manager.createQuery("select count(venda.codigo) from Venda venda where venda.banco.id = :idBanco",
		// Long.class)
		// .setParameter("idBanco", idBanco)
		// .getSingleResult();
		return 1l;
	}

}
