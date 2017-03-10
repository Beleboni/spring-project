package com.algaworks.brewer.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Comissao;
import com.algaworks.brewer.model.ItemVenda;
import com.algaworks.brewer.model.StatusVenda;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.Vendas;
import com.algaworks.brewer.service.event.venda.VendaEvent;

@Service
public class CadastroVendaService {

	@Autowired
	private Vendas vendas;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Transactional
	public Venda salvar(Venda venda) {
		// if (venda.isSalvarProibido()) {
		// throw new
		// RuntimeException("Usuário tentando salvar uma venda proibida");
		// }

		if (venda.isNova()) {
			venda.setDataCriacao(LocalDateTime.now());
		} else {
			Venda vendaExistente = vendas.findOne(venda.getCodigo());
			venda.setDataCriacao(vendaExistente.getDataCriacao());

			if (vendaExistente.isPodeAlterarStatus()) {
				StatusVenda statusNovo = venda.getStatus();
				Comissao comissao = venda.getComissao();
				venda = vendaExistente;
				venda.setStatus(statusNovo);
				venda.setComissao(comissao);
			}
		}

		this.ajustaComissoes(venda);

		// Não existe (não se preocupar)
		if (venda.getDataEntrega() != null) {
			venda.setDataHoraEntrega(LocalDateTime.of(
					venda.getDataEntrega(),
					venda.getHorarioEntrega() != null ? venda
							.getHorarioEntrega() : LocalTime.NOON));
		}

		return vendas.saveAndFlush(venda);
	}

	private void ajustaComissoes(Venda venda) {
		Comissao c = venda.getComissao();
		if (c != null) {
			c.setDataCriacao(LocalDateTime.now());
			c.setTotalVenda(venda.getValorTotal());
			c.setVenda(venda);
		
			if (c.isValido()) {
				venda.getComissoes().add(c);
			}
		}
	}

	@Transactional
	public void emitir(Venda venda) {
		venda.setStatus(StatusVenda.EMITIDA);
		salvar(venda);

		publisher.publishEvent(new VendaEvent(venda));
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

}
