package com.algaworks.brewer.repository.helper.venda;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.algaworks.brewer.dto.VendaMes;
import com.algaworks.brewer.dto.VendaOrigem;
import com.algaworks.brewer.model.Usuario;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.filter.VendaFilter;

public interface VendasQueries {

	Page<Venda> filtrar(VendaFilter filtro, Usuario usuario, Pageable pageable);
	
	public Venda buscarComItens(Long codigo);
	
	public BigDecimal valorTotalNoAno();
	public BigDecimal valorTotalNoMes();
	public BigDecimal valorTicketMedioNoAno();
	public Long countOrcamento();
	
	public List<VendaMes> totalPorMes(Long codigo);
	public List<VendaOrigem> totalPorOrigem();
	
	public Long countByBanco(Long idBanco);
	
}
