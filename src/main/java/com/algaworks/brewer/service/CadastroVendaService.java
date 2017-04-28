package com.algaworks.brewer.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.ItemVenda;
import com.algaworks.brewer.model.StatusVenda;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.Comissoes;
import com.algaworks.brewer.repository.ItensVenda;
import com.algaworks.brewer.repository.Vendas;
import com.algaworks.brewer.session.TabelasItensSession;

@Service
public class CadastroVendaService {

	@Autowired
	private Vendas vendas;
	
	@Autowired
	private Comissoes comissoes;

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private TabelasItensSession tabelaItens;
	
	@Autowired
	private ItensVenda itensVenda;

	@Transactional
	public Venda salvar(Venda venda, TabelasItensSession itens) {
		
		if (venda.getCliente().getCodigo() == null) {
			venda.setCliente(null);
		}
		
		if (venda.isNova()) {
			venda.setDataCriacao(LocalDateTime.now());
		} else {
			//Rever aqui
			Venda vendaExistente = vendas.findOne(venda.getCodigo());
			venda.setDataCriacao(vendaExistente.getDataCriacao());
			
			if (vendaExistente.isPodeAlterarStatus()) {
				StatusVenda statusNovo = venda.getStatus();
				venda = vendaExistente;
				venda.setStatus(statusNovo);
			}
		}
		
		//Mudar para baixo
		this.ajustaItensVenda(venda, itens);
		
		return vendas.saveAndFlush(venda);
	}
	
	private void ajustaItensVenda(Venda venda, TabelasItensSession itens) {
		if (!venda.isNova()) {
			itensVenda.delete(itensVenda.findByVenda(venda));
		}
	}
	

	@PreAuthorize("#venda.usuario == principal.usuario or hasRole('CANCELAR_VENDA')")
	@Transactional
	public void cancelar(Venda venda) {
		Venda vendaExistente = vendas.findOne(venda.getCodigo());

		vendaExistente.setStatus(StatusVenda.CANCELADA);
		vendas.save(vendaExistente);
	}
	
	public Venda buscar(Long codigo) {
		Venda venda = vendas.buscarComItens(codigo);
		Set<ItemVenda> set = venda.getItens().stream().collect(Collectors.toSet());
		venda.setItens(set.stream().collect(Collectors.toList()));
		return venda;
	}

	public Vendas getVendas() {
		return this.vendas;
	}
	
}
