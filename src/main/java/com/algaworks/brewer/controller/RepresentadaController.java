package com.algaworks.brewer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.model.Representada;
import com.algaworks.brewer.model.TipoPessoa;
import com.algaworks.brewer.repository.Estados;
import com.algaworks.brewer.repository.Representadas;
import com.algaworks.brewer.service.CadastroRepresentadaService;

@Controller
@RequestMapping("/representadas")
public class RepresentadaController {
	
	@Autowired
	private Estados estados;
	
	@Autowired
	private CadastroRepresentadaService cadastroRepresentadaService;
	
	@Autowired
	private Representadas representadas;
	
	@RequestMapping("/novo")
	private ModelAndView novo(Representada representada){
		ModelAndView mv = new ModelAndView("representada/CadastroRepresentada");
		mv.addObject("tiposPessoa", TipoPessoa.values());
		mv.addObject("estados", estados.findAll());
		mv.addObject(representada);
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Representada representada, BindingResult result, RedirectAttributes attributes){
		if(result.hasErrors()){
			return novo(representada);
		}
		cadastroRepresentadaService.salvar(representada);
		
		attributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
		return new ModelAndView("redirect:/representadas/novo");
	}

}
