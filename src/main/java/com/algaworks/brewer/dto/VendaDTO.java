package com.algaworks.brewer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import com.algaworks.brewer.model.StatusVenda;

public class VendaDTO {

	private Long codigo;
	private String cliente;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataFinalizacao;
	private BigDecimal valorTotal;
	private BigDecimal valorEntregue;
	private String vendedor;
	private StatusVenda status;

	public VendaDTO(Long codigo, String cliente, LocalDateTime dataCriacao,
			LocalDateTime dataFinalizacao, BigDecimal valorTotal,
			BigDecimal valorEntregue, String vendedor, StatusVenda status) {
		this.codigo = codigo;
		this.cliente = cliente;
		this.dataCriacao = dataCriacao;
		this.dataFinalizacao = dataFinalizacao;
		this.valorTotal = valorTotal;
		this.valorEntregue = valorEntregue;
		this.vendedor = vendedor;
		this.status = status;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDateTime getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(LocalDateTime dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public BigDecimal getValorTotal() {
		return Optional.ofNullable(valorTotal).orElse(BigDecimal.ZERO);
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorEntregue() {
		return Optional.ofNullable(valorEntregue).orElse(BigDecimal.ZERO);
	}

	public void setValorEntregue(BigDecimal valorEntregue) {
		this.valorEntregue = valorEntregue;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public StatusVenda getStatus() {
		return status;
	}

	public void setStatus(StatusVenda status) {
		this.status = status;
	}

}
