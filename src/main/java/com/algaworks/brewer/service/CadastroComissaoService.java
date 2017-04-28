package com.algaworks.brewer.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.brewer.model.Comissao;
import com.algaworks.brewer.model.StatusVenda;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.Comissoes;
import com.algaworks.brewer.service.exception.ComissaoMaiorQueVendaException;

@Service
public class CadastroComissaoService {
	
	@Autowired
	private Comissoes comissoes;
	
	@Autowired 
	private CadastroVendaService cadastroVendaService;
	
	@Transactional(rollbackOn = ComissaoMaiorQueVendaException.class)
	public void salvar(Comissao comissao) throws ComissaoMaiorQueVendaException {
		Venda venda = cadastroVendaService.getVendas().buscarComItens(
				comissao.getVenda().getCodigo());

		Double totalComissoes = comissoes.findByVenda(venda).stream()
				.mapToDouble(c -> c.getTotalEntregue().doubleValue()).sum() + comissao.getTotalEntregue().doubleValue();

		if (totalComissoes > venda.getValorTotalItens().doubleValue()) {
			throw new ComissaoMaiorQueVendaException("O valor é maior que o total do pedido.");
		}

		if (StatusVenda.ENTREGUE_PARCIALMENTE.equals(venda.getStatus())
				&& totalComissoes == venda.getValorTotalItens().doubleValue()) {
			venda.setStatus(StatusVenda.CONCLUIDO);
			cadastroVendaService.getVendas().save(venda);
		}
		
		comissao.setDataCriacao(LocalDateTime.now());
		comissoes.save(comissao);
	}

}
