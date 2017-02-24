package com.algaworks.brewer.session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.algaworks.brewer.model.ItemPedido;
import com.algaworks.brewer.model.Produto;

public class TabelaItensPedido {
	
	private String uuid;
	private List<ItemPedido> itens = new ArrayList<>();
	
	public TabelaItensPedido(String uuid) {
		this.uuid = uuid;
	}

	public BigDecimal getValorTotal() {
		return itens.stream()
				.map(ItemPedido::getValorTotal)
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);
	}
	
	public void adicionarItem(Produto produto, Integer quantidade) {
		Optional<ItemPedido> itemPedidoOptional = buscarItemPorProduto(produto);
		
		ItemPedido itemPedido = null;
		if (itemPedidoOptional.isPresent()) {
			itemPedido = itemPedidoOptional.get();
			itemPedido.setQuantidade(itemPedido.getQuantidade() + quantidade);
		} else {
			itemPedido = new ItemPedido();
			itemPedido.setProduto(produto);
			itemPedido.setQuantidade(quantidade);
			itemPedido.setValorUnitario(produto.getValor());
			itens.add(0, itemPedido);
		}
	}
	
	public void alterarQuantidadeItens(Produto produto, Integer quantidade) {
		ItemPedido itemPedido = buscarItemPorProduto(produto).get();
		itemPedido.setQuantidade(quantidade);
	}
	
	public void excluirItem(Produto produto) {
		int indice = IntStream.range(0, itens.size())
				.filter(i -> itens.get(i).getProduto().equals(produto))
				.findAny().getAsInt();
		itens.remove(indice);
	}
	
	public int total() {
		return itens.size();
	}

	public List<ItemPedido> getItens() {
		return itens;
	}
	
	private Optional<ItemPedido> buscarItemPorProduto(Produto produto) {
		return itens.stream()
				.filter(i -> i.getProduto().equals(produto))
				.findAny();
	}

	public String getUuid() {
		return uuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TabelaItensPedido other = (TabelaItensPedido) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
}
