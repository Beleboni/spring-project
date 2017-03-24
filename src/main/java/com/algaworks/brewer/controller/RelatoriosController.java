package com.algaworks.brewer.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.algaworks.brewer.dto.PeriodoRelatorio;
import com.algaworks.brewer.repository.Clientes;

@Controller
@RequestMapping("/relatorios")
public class RelatoriosController {
	
	@Autowired
	private Clientes clientes;
	
	@GetMapping("/vendasEmitidas")
	public ModelAndView relatorioVendasEmitidas() {
		ModelAndView mv = new ModelAndView("relatorio/RelatorioVendasEmitidas");
		mv.addObject(new PeriodoRelatorio());
		return mv;
	}
	
	@PostMapping("/vendasEmitidas")
	public ModelAndView gerarRelatorioVendasEmitidas(PeriodoRelatorio periodoRelatorio) {
		Map<String, Object> parametros = new HashMap<>();
		
		Date dataInicio = Date.from(LocalDateTime.of(periodoRelatorio.getDataInicio(), LocalTime.of(0, 0, 0))
				.atZone(ZoneId.systemDefault()).toInstant());
		Date dataFim = Date.from(LocalDateTime.of(periodoRelatorio.getDataFim(), LocalTime.of(23, 59, 59))
				.atZone(ZoneId.systemDefault()).toInstant());
		
		parametros.put("format", "pdf");
		parametros.put("data_inicio", dataInicio);
		parametros.put("data_fim", dataFim);
		
		return new ModelAndView("relatorio_vendas_emitidas", parametros);
	}
	
	@GetMapping("/vendasEfetuadas")
	public ModelAndView vendasEfetuada() {
		ModelAndView mv = new ModelAndView("relatorio/RelatorioVendasEfetuadas");
		mv.addObject("clientes", clientes.findAll());
		mv.addObject(new PeriodoRelatorio());
		return mv;
	}
	
	@PostMapping("/vendasEfetuadas")
	public ModelAndView vendasEfetuadas(PeriodoRelatorio periodoRelatorio) {
		Map<String, Object> parametros = new HashMap<>();
		
		Date dataInicio = Date.from(LocalDateTime.of(periodoRelatorio.getDataInicio(), LocalTime.of(0, 0, 0))
				.atZone(ZoneId.systemDefault()).toInstant());
		Date dataFim = Date.from(LocalDateTime.of(periodoRelatorio.getDataFim(), LocalTime.of(23, 59, 59))
				.atZone(ZoneId.systemDefault()).toInstant());
		Long codigoCliente = periodoRelatorio.getCodigoCliente();
		
		parametros.put("format", "pdf");
		parametros.put("inicio", dataInicio);
		parametros.put("termino", dataFim);
		parametros.put("codigoCliente", codigoCliente);
		return new ModelAndView("relatorio_pedidos_efetuados", parametros);
	}
	
	@GetMapping("/pedidoPronto/{codigo}")
	public ModelAndView pedidoPronto(@PathVariable Long codigo) {
		Map<String, Object> parametros = new HashMap<>();
		
		
		parametros.put("format", "pdf");
		parametros.put("codigoVenda", codigo);
		parametros.put("relatorio_item_pedido", arg1)
		return new ModelAndView("relatorio_pedido_pronto", parametros);
	}

}
