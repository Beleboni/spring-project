package com.algaworks.brewer.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Banco;
import com.algaworks.brewer.repository.Bancos;
import com.algaworks.brewer.repository.filter.BancoFilter;
import com.algaworks.brewer.service.CadastroBancoService;

@Controller
@RequestMapping("/bancos")
public class BancosController {
	
	@Autowired
	private Bancos bancos;
	
	@Autowired
	private CadastroBancoService cadastroBancoService;
	
	
	@GetMapping
	public ModelAndView pesquisar(BancoFilter bancoFilter, BindingResult result, @PageableDefault(size = 10) Pageable pageable,
			HttpServletRequest httpServletRequest){
		ModelAndView mv = new ModelAndView("banco/PesquisaBancos");
		mv.addObject("bancos", bancos.findAll());
		
		PageWrapper<Banco> paginaWrapper = new PageWrapper<>(bancos.filtrar(bancoFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	

}
