package com.algaworks.brewer.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Produto;
import com.algaworks.brewer.repository.Produtos;
import com.algaworks.brewer.repository.Representadas;
import com.algaworks.brewer.repository.filter.ProdutoFilter;
import com.algaworks.brewer.service.CadastroProdutoService;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private Produtos produtos;
	
	@Autowired
	private Representadas representadas;
	
	@Autowired
	private CadastroProdutoService cadastroProdutoService;
	
	
	@RequestMapping("/novo")
	public ModelAndView novo(Produto produto){
		ModelAndView mv = new ModelAndView("produto/cadastroProduto");
		mv.addObject("representadas", representadas.findAll());
		mv.addObject(produto);
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Produto produto, BindingResult result, RedirectAttributes attributes){
		if(result.hasErrors()){
			return novo(produto);
		}
		
		cadastroProdutoService.salvar(produto);
		
		attributes.addFlashAttribute("mensagem", "Produto cadastrado com sucesso");
		return new ModelAndView("redirect:/produtos/novo");
	}
	
	
	@GetMapping
	public ModelAndView pesquisar(ProdutoFilter produtoFilter, BindingResult resut, @PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest){
		ModelAndView mv = new ModelAndView("produto/PesquisaProdutos");
		mv.addObject("produtos", produtos.findAll());
		
		PageWrapper<Produto> paginaWrapper = new PageWrapper<>(produtos.filtrar(produtoFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
}
