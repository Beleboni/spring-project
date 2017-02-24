package com.algaworks.brewer.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.algaworks.brewer.controller.validator.PedidoValidator;
import com.algaworks.brewer.model.Pedido;
import com.algaworks.brewer.session.TabelasItensPedidoSession;

@Controller
@RequestMapping("/pedidos")
public class PedidosController {
	
	//private Produtos produtos;
	
	private TabelasItensPedidoSession tabelaItens;
	
	//private CadastroPedidoService cadastroPedidoService;
	
	private PedidoValidator pedidoValidator;
	
	//private Pedidos pedidos;
	
	@InitBinder("pedido")
	public void inicializarValidador(WebDataBinder binder) {
		binder.setValidator(pedidoValidator);
	}
	
	public ModelAndView novo(Pedido pedido){
		ModelAndView mv = new ModelAndView("pedido/CadastroPedido");
		
		setUuid(pedido);
		
		mv.addObject("itens", pedido.getItens());
		mv.addObject("valorTotalItens", tabelaItens.getValorTotal(pedido.getUuid()));
		return mv;		
	}
	
	private void setUuid(Pedido pedido) {
		if (StringUtils.isEmpty(pedido.getUuid())) {
			pedido.setUuid(UUID.randomUUID().toString());
		}
	}
	
	

}
