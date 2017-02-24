package com.algaworks.brewer.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Pedido;
import com.algaworks.brewer.repository.Pedidos;

@Service
public class CadastroPedidoService {

	@Autowired
	private Pedidos pedidos;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Transactional
	public Pedido salvar(Pedido pedido){
		if(pedido.isSalvarProibido()){
			throw new RuntimeException("Usuário tentando salvar um pedido proibido");
		}	
		
		
		if(pedido.isNovo()){
			pedido.setDataCriacao(LocalDateTime.now());
		} else{
			Pedido pedidoExistente = pedidos.findOne(pedido.getCodigo());
			pedido.setDataCriacao(pedidoExistente.getDataCriacao());
		}
		
		return pedidos.saveAndFlush(pedido);
	}
}
