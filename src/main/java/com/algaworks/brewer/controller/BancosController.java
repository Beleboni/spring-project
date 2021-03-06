package com.algaworks.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Banco;
import com.algaworks.brewer.repository.Bancos;
import com.algaworks.brewer.repository.filter.BancoFilter;
import com.algaworks.brewer.security.UsuarioSistema;
import com.algaworks.brewer.service.CadastroBancoService;
import com.algaworks.brewer.service.exception.DescricaoBancoJaCadastradoException;

@Controller
@RequestMapping("/bancos")
public class BancosController {

	@Autowired
	private Bancos bancos;

	@Autowired
	private CadastroBancoService cadastroBancoService;

	@RequestMapping("/novo")
	public ModelAndView novo(Banco banco) {
		ModelAndView mv = new ModelAndView("banco/cadastroBanco");
		mv.addObject(banco);
		return mv;
	}

	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Banco banco, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(banco);
		}
		cadastroBancoService.salvar(banco);

		attributes.addFlashAttribute("mensagem",
				"Banco vadastrado com sucesso!");
		return new ModelAndView("redirect:/bancos/novo");
	}

	@GetMapping
	public ModelAndView pesquisar(BancoFilter bancoFilter,
			BindingResult result,
			@PageableDefault(size = 10) Pageable pageable,
			HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("banco/PesquisaBancos");
		mv.addObject("bancos", bancos.findAll());

		PageWrapper<Banco> paginaWrapper = new PageWrapper<>(bancos.filtrar(
				bancoFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Banco banco = bancos.findOne(codigo);
		return novo(banco);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<?> salvar(
			@RequestBody @Valid Banco banco, BindingResult result,
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {

		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(
					result.getFieldError("descricao").getDefaultMessage());
		}

		try {
			banco.setCodEmpresa(1l);
			banco = cadastroBancoService.salvar(banco);
			return ResponseEntity.ok(banco);
		} catch (DescricaoBancoJaCadastradoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/excluir/{codigo}")
	public ModelAndView excluir(@PathVariable Long codigo,
			RedirectAttributes attributes) {
		try {
			cadastroBancoService.excluir(codigo);
			attributes.addFlashAttribute("mensagem",
					"Banco excluido com sucesso!");
		} catch (RuntimeException e) {
			attributes.addFlashAttribute("mensagemErro", e.getMessage());
		}

		return new ModelAndView("redirect:/bancos");
	}

}
