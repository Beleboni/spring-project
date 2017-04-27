package com.algaworks.brewer.session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.ItemVenda;

class TabelaItensVenda {

	private String uuid;
	private List<ItemVenda> itens = new ArrayList<>();

	public TabelaItensVenda(String uuid) {
		this.uuid = uuid;
	}

	public BigDecimal getValorTotal() {
		return itens.stream().map(ItemVenda::getValorTotal)
				.reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	}

	public void adicionarItem(Cerveja cerveja, Integer quantidade, Float valor,
			String observacao) {
		ItemVenda itemVenda = new ItemVenda();
		itemVenda.setUuid(UUID.randomUUID().toString());
		this.setItemVenda(itemVenda, cerveja, quantidade, valor, observacao);
		itens.add(itemVenda);
	}

	private void setItemVenda(ItemVenda itemVenda, Cerveja cerveja,
			Integer quantidade, Float valor, String observacao) {
		itemVenda.setCerveja(cerveja);
		//itemVenda.setQuantidade(quantidade);
		itemVenda.setValorUnitario(BigDecimal.valueOf(valor));
		itemVenda.setObservacoes(observacao);
	}

	public void alterarItens(Cerveja cerveja, String uuidItem, Integer quantidade,
			Float valor, String observacao) {
		ItemVenda itemVenda = buscarItemPorUuid(uuidItem).get();
		this.setItemVenda(itemVenda, cerveja, quantidade, valor, observacao);
	}

	public void excluirItem(String uuidItem) {
		int indice = IntStream.range(0, itens.size())
				.filter(i -> itens.get(i).getUuid().equals(uuidItem))
				.findAny().getAsInt();
		itens.remove(indice);
	}

	public int total() {
		return itens.size();
	}

	public List<ItemVenda> getItens() {
		return itens;
	}

	private Optional<ItemVenda> buscarItemPorUuid(String uuid) {
		return itens.stream().filter(i -> i.getUuid().equals(uuid)).findAny();
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
		TabelaItensVenda other = (TabelaItensVenda) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

}
