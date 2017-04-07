package com.algaworks.brewer.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.controller.validator.VendaValidator;
import com.algaworks.brewer.dto.VendaMes;
import com.algaworks.brewer.dto.VendaOrigem;
import com.algaworks.brewer.mail.Mailer;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.ItemVenda;
import com.algaworks.brewer.model.StatusVenda;
import com.algaworks.brewer.model.TipoPessoa;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.Bancos;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.repository.Vendas;
import com.algaworks.brewer.repository.filter.VendaFilter;
import com.algaworks.brewer.security.UsuarioSistema;
import com.algaworks.brewer.service.CadastroVendaService;
import com.algaworks.brewer.session.TabelasItensSession;

@Controller
@RequestMapping("/vendas")
public class VendasController {

	@Autowired
	private Cervejas cervejas;

	@Autowired
	private TabelasItensSession tabelaItens;

	@Autowired
	private CadastroVendaService cadastroVendaService;

	@Autowired
	private VendaValidator vendaValidator;

	@Autowired
	private Vendas vendas;

	@Autowired
	private Bancos bancos;

	@Autowired
	private Mailer mailer;

	@InitBinder("venda")
	public void inicializarValidador(WebDataBinder binder) {
		binder.setValidator(vendaValidator);
	}

	@GetMapping("/nova")
	public ModelAndView nova(Venda venda) {
		ModelAndView mv = new ModelAndView("venda/CadastroVenda");

		setUuid(venda);
		mv.addObject("bancos", bancos.bancosAtivo(1l));
		mv.addObject("itens", tabelaItens.getItens(venda.getUuid()));
		mv.addObject("valorFrete", venda.getValorFrete());
		mv.addObject("valorDesconto", venda.getValorDesconto());
		mv.addObject("valorTotalItens",
				tabelaItens.getValorTotal(venda.getUuid()));
		mv.addObject("statusVenda", StatusVenda.values());

		return mv;
	}

	@PostMapping("/nova")
	public ModelAndView salvar(Venda venda, BindingResult result,
			RedirectAttributes attributes) {
		validarVenda(venda, result);
		if (result.hasErrors()) {
			return nova(venda);
		}
		cadastroVendaService.salvar(venda, tabelaItens);
		attributes.addFlashAttribute("mensagem", "Venda salva com sucesso");
		return new ModelAndView("redirect:/vendas/nova");
	}

	// @PostMapping(value = "/nova", params = "salvar")
	// public ModelAndView salvar(Venda venda, BindingResult result,
	// RedirectAttributes attributes) {
	// validarVenda(venda, result);
	// if (result.hasErrors()) {
	// return nova(venda);
	// }
	// cadastroVendaService.salvar(venda);
	// attributes.addFlashAttribute("mensagem", "Venda salva com sucesso");
	// return new ModelAndView("redirect:/vendas/nova");
	// }

	// @PostMapping(value = "/nova", params = "emitir")
	// public ModelAndView emitir(Venda venda, BindingResult result,
	// RedirectAttributes attributes,
	// @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
	// validarVenda(venda, result);
	// if (result.hasErrors()) {
	// return nova(venda);
	// }
	//
	// venda.setUsuario(usuarioSistema.getUsuario());
	//
	// cadastroVendaService.emitir(venda);
	// attributes.addFlashAttribute("mensagem", "Venda emitida com sucesso");
	// return new ModelAndView("redirect:/vendas/nova");
	// }
	//
	// @PostMapping(value = "/nova", params = "enviarEmail")
	// public ModelAndView enviarEmail(Venda venda, BindingResult result,
	// RedirectAttributes attributes,
	// @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
	// validarVenda(venda, result);
	// if (result.hasErrors()) {
	// return nova(venda);
	// }
	//
	// venda.setUsuario(usuarioSistema.getUsuario());
	//
	// venda = cadastroVendaService.salvar(venda);
	// mailer.enviar(venda);
	//
	// attributes.addFlashAttribute("mensagem", String.format(
	// "Venda nº %d salva com sucesso e e-mail enviado",
	// venda.getCodigo()));
	// return new ModelAndView("redirect:/vendas/nova");
	// }

	@PostMapping("/item/{codigoCerveja}/adicionar")
	public ModelAndView adicionarItem(
			@PathVariable("codigoCerveja") Cerveja cerveja, Integer quantidade,
			Float valor, String observacao, String uuid) {
		tabelaItens.adicionarItem(uuid, cerveja, quantidade, valor, observacao);
		return mvTabelaItensVenda(uuid);
	}

	@PutMapping("/item/{codigoCerveja}/alterar")
	public ModelAndView alterarItem(
			@PathVariable("codigoCerveja") Cerveja cerveja, Integer quantidade,
			Float valor, String observacao, String uuid, String uuidItem) {
		tabelaItens.alterarItem(uuid, cerveja, uuidItem, quantidade, valor,
				observacao);
		return mvTabelaItensVenda(uuid);
	}

	@DeleteMapping("/item/{uuid}/excluir/{uuidItem}")
	public ModelAndView excluirItem(@PathVariable String uuid,
			@PathVariable String uuidItem) {
		tabelaItens.excluirItem(uuid, uuidItem);
		return mvTabelaItensVenda(uuid);
	}

	@GetMapping
	public ModelAndView pesquisar(VendaFilter vendaFilter,
			@PageableDefault(size = 10) Pageable pageable,
			HttpServletRequest httpServletRequest,
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("/venda/PesquisaVendas");
		mv.addObject("todosStatus", StatusVenda.values());
		mv.addObject("tiposPessoa", TipoPessoa.values());

		PageWrapper<Venda> paginaWrapper = new PageWrapper<>(vendas.filtrar(
				vendaFilter, usuarioSistema.getUsuario(), pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Venda venda = cadastroVendaService.buscar(codigo);

		setUuid(venda);
		for (ItemVenda item : venda.getItens()) {
			tabelaItens.adicionarItem(venda.getUuid(), item.getCerveja(),
					item.getQuantidade(), item.getValorUnitario(),
					item.getObservacoes());
		}

		ModelAndView mv = nova(venda);
		mv.addObject(venda);
		return mv;
	}

	@PostMapping(value = "/nova", params = "cancelar")
	public ModelAndView cancelar(Venda venda, BindingResult result,
			RedirectAttributes attributes,
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		try {
			cadastroVendaService.cancelar(venda);
		} catch (AccessDeniedException e) {
			return new ModelAndView("/403");
		}

		attributes.addFlashAttribute("mensagem", "Venda cancelada com sucesso");
		return new ModelAndView("redirect:/vendas/" + venda.getCodigo());
	}

	@GetMapping("/totalPorMes")
	public @ResponseBody List<VendaMes> listarTotalVendaPorMes() {
		return vendas.totalPorMes(1l);
	}

	@GetMapping("/porOrigem")
	public @ResponseBody List<VendaOrigem> vendasPorNacionalidade() {
		return this.vendas.totalPorOrigem();
	}

	private ModelAndView mvTabelaItensVenda(String uuid) {
		ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
		mv.addObject("itens", tabelaItens.getItens(uuid));
		mv.addObject("valorTotal", tabelaItens.getValorTotal(uuid));
		return mv;
	}

	private void validarVenda(Venda venda, BindingResult result) {
		venda.adicionarItens(tabelaItens.getItens(venda.getUuid()));
		venda.calcularValorTotal();

		vendaValidator.validate(venda, result);
	}

	private void setUuid(Venda venda) {
		if (StringUtils.isEmpty(venda.getUuid())) {
			venda.setUuid(UUID.randomUUID().toString());
		}
	}

}
