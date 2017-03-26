package com.algaworks.brewer.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.brewer.model.ItemVenda;
import com.algaworks.brewer.model.Venda;

public interface ItensVenda extends JpaRepository<ItemVenda, Long> {
	
	Set<ItemVenda> findByVenda(Venda venda);
	
}
