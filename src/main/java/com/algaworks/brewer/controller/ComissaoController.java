package com.algaworks.brewer.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.model.Comissao;
import com.algaworks.brewer.model.ItemVenda;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.Comissoes;
import com.algaworks.brewer.service.CadastroVendaService;

@Controller
@RequestMapping("/comissoes")
public class ComissaoController {
		
	@Autowired
	private CadastroVendaService cadastroVendaService;
	@Autowired
	private Comissoes comissoes;
	
	@GetMapping("/{codigo}")
	public ModelAndView visualizar(@PathVariable Long codigo){
		Venda venda = cadastroVendaService.buscar(codigo);
		Comissao comissao = new Comissao();
		comissao.setVenda(venda);
		comissao.setTotalVenda(venda.getValorTotal());
		ModelAndView mv = new ModelAndView("comissao/ComissaoVenda");
		mv.addObject("comissoes", comissoes.findByVenda(venda));
		mv.addObject(venda);
		mv.addObject(comissao);
		return mv;
	}
	
	@Transactional
	@PostMapping("/salvar")
	public ModelAndView salvar(Comissao comissao, RedirectAttributes attributes) {
		comissao.setDataCriacao(LocalDateTime.now());
		// comissao.setVenda(new Venda(comissao.getVenda().getCodigo()));
		comissao.getVenda().setItens(new ArrayList<ItemVenda>());
		comissoes.save(comissao);
		attributes.addFlashAttribute("mensagem", "Comissao salva com sucesso");
		return visualizar(comissao.getVenda().getCodigo());
	}
	
}
