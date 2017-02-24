package com.algaworks.brewer.session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.algaworks.brewer.model.ItemPedido;
import com.algaworks.brewer.model.Produto;

@SessionScope
@Component
public class TabelasItensPedidoSession {

	private Set<TabelaItensPedido> tabelas = new HashSet<>();

	public void adicionarItem(String uuid, Produto produto, int quantidade) {
		TabelaItensPedido tabela = buscarTabelaPorUuid(uuid);
		tabela.adicionarItem(produto, quantidade);
		tabelas.add(tabela);
	}

	public void alterarQuantidadeItens(String uuid, Produto produto,
			Integer quantidade) {
		TabelaItensPedido tabela = buscarTabelaPorUuid(uuid);
		tabela.alterarQuantidadeItens(produto, quantidade);
	}

	public void excluirItem(String uuid, Produto produto) {
		TabelaItensPedido tabela = buscarTabelaPorUuid(uuid);
		tabela.excluirItem(produto);
	}

	public List<ItemPedido> getItens(String uuid) {
		return buscarTabelaPorUuid(uuid).getItens();
	}

	public Object getValorTotal(String uuid) {
		return buscarTabelaPorUuid(uuid).getValorTotal();
	}

	private TabelaItensPedido buscarTabelaPorUuid(String uuid) {
		TabelaItensPedido tabela = tabelas.stream()
				.filter(t -> t.getUuid().equals(uuid)).findAny()
				.orElse(new TabelaItensPedido(uuid));
		return tabela;
	}
}
