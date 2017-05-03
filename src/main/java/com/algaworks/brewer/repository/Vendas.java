package com.algaworks.brewer.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.algaworks.brewer.model.StatusVenda;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.helper.venda.VendasImpl;
import com.algaworks.brewer.repository.helper.venda.VendasQueries;

@Repository
public interface Vendas extends JpaRepository<Venda, Long>, VendasQueries {

	//Ver aqui
	@Query(value = "select new com.algaworks.brewer.dto.VendaDTO(v.codigo, c.nome, v.dataCriacao, v.dataFinalizacao, "
			+ "(select sum(iv.quantidade * iv.valorUnitario) from ItemVenda iv where iv.venda = v), "
			+ "(select sum(cm.totalEntregue) from Comissao cm where cm.venda = v), u.nome, v.status) "
			+ VendasImpl.CLAUSE_FROM_AND_WHERE_FILTRAR, countQuery = "select count(v) "
			+ VendasImpl.CLAUSE_FROM_AND_WHERE_FILTRAR)
	Page<Venda> filtrar(LocalDateTime inicio, LocalDateTime fim, StatusVenda status, String nomeCliente, Pageable pageable);

}
