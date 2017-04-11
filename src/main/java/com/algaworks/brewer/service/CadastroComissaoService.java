package com.algaworks.brewer.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.brewer.model.Comissao;
import com.algaworks.brewer.model.StatusVenda;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.Comissoes;

@Service
public class CadastroComissaoService {
	
	@Autowired
	private Comissoes comissoes;
	
	@Autowired 
	private CadastroVendaService cadastroVendaService;
	
	@Transactional
	public void salvar(Comissao comissao){
		comissao.setDataCriacao(LocalDateTime.now());
		comissoes.save(comissao);
		
		Venda venda = cadastroVendaService.getVendas().buscarComItens(
				comissao.getVenda().getCodigo());

		Double totalComissoes = comissoes.findByVenda(venda).stream()
				.mapToDouble(c -> c.getTotalEntregue().doubleValue()).sum();

		if (StatusVenda.ENTREGUE_PARCIALMENTE.equals(venda.getStatus())
				&& totalComissoes == venda.getValorTotal().doubleValue()) {
			venda.setStatus(StatusVenda.CONCLUIDO);
			cadastroVendaService.getVendas().save(venda);
		}
	}

}
