package com.algaworks.brewer.session;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.ItemVenda;

@SessionScope
@Component
public class TabelasItensSession {

	private Set<TabelaItensVenda> tabelas = new HashSet<>();

	public void adicionarItem(String uuid, Cerveja cerveja, Integer quantidade, Float valor, String observacoes) {
		TabelaItensVenda tabela = buscarTabelaPorUuid(uuid);
		tabela.adicionarItem(cerveja, quantidade, valor, observacoes);
		tabelas.add(tabela);
	}
	
	public void adicionarItem(String uuid, Cerveja cerveja, Integer quantidade, BigDecimal valor, String observacoes) {
		this.adicionarItem(uuid, cerveja, quantidade, valor.floatValue(), observacoes);
	}

	public void alterarItem(String uuid, Cerveja cerveja, String uuidItem, Integer quantidade, Float valor, String observacao) {
		TabelaItensVenda tabela = buscarTabelaPorUuid(uuid);
		tabela.alterarItens(cerveja, uuidItem ,quantidade, valor, observacao);
	}

	public void excluirItem(String uuid, Cerveja cerveja) {
		TabelaItensVenda tabela = buscarTabelaPorUuid(uuid);
		tabela.excluirItem(cerveja);
	}

	public List<ItemVenda> getItens(String uuid) {
		return buscarTabelaPorUuid(uuid).getItens();
	}
	
	public Object getValorTotal(String uuid) {
		return buscarTabelaPorUuid(uuid).getValorTotal();
	}
	
	private TabelaItensVenda buscarTabelaPorUuid(String uuid) {
		TabelaItensVenda tabela = tabelas.stream()
				.filter(t -> t.getUuid().equals(uuid))
				.findAny()
				.orElse(new TabelaItensVenda(uuid));
		return tabela;
	}

	
}
