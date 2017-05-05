package com.algaworks.brewer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.controller.validator.VendaValidator;
import com.algaworks.brewer.dto.VendaMes;
import com.algaworks.brewer.dto.VendaOrigem;
import com.algaworks.brewer.mail.Mailer;
import com.algaworks.brewer.model.ItemVenda;
import com.algaworks.brewer.model.StatusVenda;
import com.algaworks.brewer.model.TipoPessoa;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.Bancos;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.repository.ItensVenda;
import com.algaworks.brewer.repository.Vendas;
import com.algaworks.brewer.repository.filter.VendaFilter;
import com.algaworks.brewer.security.UsuarioSistema;
import com.algaworks.brewer.service.CadastroVendaService;
import com.algaworks.brewer.session.TabelasItensSession;
import com.google.common.collect.Lists;

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
	private ItensVenda itensVenda;

	@Autowired
	private Mailer mailer;

	@InitBinder("venda")
	public void inicializarValidador(WebDataBinder binder) {
		binder.setValidator(vendaValidator);
	}

	@Transactional
	@GetMapping("/nova")
	public ModelAndView nova() {
		Venda venda = vendas.save(new Venda());
		return new ModelAndView("redirect:/vendas/" + venda.getCodigo()
				+ "/nova");
	}

	@GetMapping("/{codigo}/nova")
	public ModelAndView iniciar(@PathVariable("codigo") Venda venda) {
		ModelAndView mv = new ModelAndView("venda/CadastroVenda");
		Venda vendaBanco = vendas.buscarComItens(venda.getCodigo());
		mv.addObject("bancos", bancos.bancosAtivo(1l));
		mv.addObject("itens", vendaBanco.getItens());
		mv.addObject("valorTotalItens", vendaBanco.getValorTotalItens());
		mv.addObject("statusVenda", StatusVenda.values());
		mv.addObject("item", new ItemVenda(venda));
		mv.addObject("venda", vendaBanco);
		return mv;
	}
	
	@GetMapping("/{codigo}/ver")
	public ModelAndView ver(@PathVariable("codigo") Venda venda) {
		ModelAndView mv = new ModelAndView("venda/CadastroVenda");
		Venda vendaBanco = vendas.buscarComItens(venda.getCodigo());
		mv.addObject("bancos", bancos.bancosAtivo(1l));
		mv.addObject("itens", vendaBanco.getItens());
		mv.addObject("valorTotalItens", vendaBanco.getValorTotalItens());
		mv.addObject("statusVenda", StatusVenda.values());
		mv.addObject("item", new ItemVenda(venda));
		mv.addObject("venda", vendaBanco);
		return mv;
	}

	@RequestMapping(value = "/item", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public @ResponseBody ItemVenda getItem(Long codigo) {
		return itensVenda.findOne(codigo);
	}

	@Transactional
	@PostMapping(value = "/item/salvar")
	public ModelAndView saveItem(ItemVenda item) {
		ModelAndView mv = new ModelAndView("venda/ItemVendaTabela");
		mv.addObject("itens", Lists.newArrayList(itensVenda.save(item)));
		return mv;
	}

	@Transactional
	@DeleteMapping(value = "/item/excluir")
	public @ResponseBody ResponseEntity<String> deleteItem(ItemVenda item) {
		itensVenda.delete(item);
		return ResponseEntity.ok("Item excluído com sucesso");
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
				vendaFilter, usuarioSistema.getUsuario(), pageable),
				httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	@Transactional
	@DeleteMapping("/excluir/{codigo}")
	public ModelAndView excluir(@PathVariable("codigo") Venda venda,
			RedirectAttributes attributes) {
		vendas.delete(venda);
		attributes.addFlashAttribute("mensagem", "Venda excluída com sucesso");
		return new ModelAndView("redirect:/vendas/");
	}

	@GetMapping("/totalPorMes")
	public @ResponseBody List<VendaMes> listarTotalVendaPorMes() {
		return vendas.totalPorMes(1l);
	}

	@GetMapping("/porOrigem")
	public @ResponseBody List<VendaOrigem> vendasPorNacionalidade() {
		return this.vendas.totalPorOrigem();
	}

}
