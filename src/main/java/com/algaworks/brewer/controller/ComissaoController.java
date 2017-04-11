package com.algaworks.brewer.controller;

import java.util.List;

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
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.Comissoes;
import com.algaworks.brewer.service.CadastroComissaoService;
import com.algaworks.brewer.service.CadastroVendaService;
import com.ibm.icu.math.BigDecimal;

@Controller
@RequestMapping("/comissoes")
public class ComissaoController {

	@Autowired
	private CadastroVendaService cadastroVendaService;
	
	@Autowired
	private CadastroComissaoService CadastroComissaoService;
	
	@Autowired
	private Comissoes comissoes;

	@GetMapping("/{codigo}")
	public ModelAndView visualizar(@PathVariable Long codigo) {
		ModelAndView mv = new ModelAndView("comissao/ComissaoVenda");

		Venda venda = cadastroVendaService.buscar(codigo);

		Comissao comissao = new Comissao();
		comissao.setVenda(venda);
		comissao.setTotalVenda(venda.getValorTotal());

		List<Comissao> cms = comissoes.findByVenda(venda);

		Double totalVenda = venda.getValorTotal().doubleValue();

		Double totalComissoes = cms.stream()
				.mapToDouble(c -> c.getTotalEntregue().doubleValue()).sum();

		mv.addObject("exibeCampos", !totalVenda.equals(totalComissoes));
		mv.addObject("comissoes", cms);
		mv.addObject("total", BigDecimal.valueOf(totalComissoes));
		mv.addObject(comissao);
		mv.addObject(venda);
		return mv;
	}

	@Transactional
	@GetMapping("/excluir/{codigo}")
	public ModelAndView excluir(@PathVariable Long codigo, Long codVenda,
			RedirectAttributes attributes) {
		comissoes.delete(codigo);
		attributes.addFlashAttribute("mensagem",
				"Comissao excluida com sucesso!");
		return new ModelAndView("redirect:/comissoes/" + codVenda);
	}

	@PostMapping("/salvar")
	public ModelAndView salvar(Comissao comissao, RedirectAttributes attributes) {
		
		CadastroComissaoService.salvar(comissao);		

		attributes.addFlashAttribute("mensagem", "Comissao salva com sucesso");
		return new ModelAndView("redirect:/comissoes/"
				+ comissao.getVenda().getCodigo());
	}

}
