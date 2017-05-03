package com.algaworks.brewer.repository.filter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.lang.StringUtils;

import com.algaworks.brewer.model.StatusVenda;

public class VendaFilter {

	private Long codigo;
	private StatusVenda status;

	private LocalDate desde = LocalDate.now().minusDays(30);
	private LocalDate ate = LocalDate.now().plusDays(30);
	private BigDecimal valorMinimo;
	private BigDecimal valorMaximo;

	private String nomeCliente;
	private String cpfOuCnpjCliente;

	public LocalDateTime getDesdeWithTime() {
		return this.desde.atTime(0, 0, 0, 0);
	}
	
	public LocalDateTime getAteWithTime() {
		return this.ate.atTime(23, 59, 59, 999);
	}
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public StatusVenda getStatus() {
		return status;
	}

	public void setStatus(StatusVenda status) {
		this.status = status;
	}

	public LocalDate getDesde() {
		return desde;
	}

	public void setDesde(LocalDate desde) {
		this.desde = desde;
	}

	public LocalDate getAte() {
		return ate;
	}

	public void setAte(LocalDate ate) {
		this.ate = ate;
	}

	public BigDecimal getValorMinimo() {
		return valorMinimo;
	}

	public void setValorMinimo(BigDecimal valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public BigDecimal getValorMaximo() {
		return valorMaximo;
	}

	public void setValorMaximo(BigDecimal valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	public String getNomeCliente() {
		return StringUtils.isEmpty(nomeCliente) ? null : nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getCpfOuCnpjCliente() {
		return cpfOuCnpjCliente;
	}

	public void setCpfOuCnpjCliente(String cpfOuCnpjCliente) {
		this.cpfOuCnpjCliente = cpfOuCnpjCliente;
	}

}
