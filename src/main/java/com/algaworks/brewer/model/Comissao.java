package com.algaworks.brewer.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "comissao")
@DynamicUpdate
public class Comissao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao;
	
	@ManyToOne
	@JoinColumn(name = "codigo_venda")
	private Venda venda;
	
	@Column(name = "total_venda")
	private BigDecimal totalVenda;
	
	@Column(name = "total_entregue")
	private BigDecimal totalEntregue;
	
	@Column(name = "percentual")
	private BigDecimal percentual;
	
//	@Column(name = "pecentual")
//	private BigDecimal percentualRepresentada;
	
	public BigDecimal getTotal() {
		return this.totalEntregue.multiply(this.percentual.divide(BigDecimal.valueOf(100)));
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public BigDecimal getTotalVenda() {
		return totalVenda;
	}

	public void setTotalVenda(BigDecimal totalVenda) {
		this.totalVenda = totalVenda;
	}

	public BigDecimal getTotalEntregue() {
		return totalEntregue;
	}

	public void setTotalEntregue(BigDecimal totalEntregue) {
		this.totalEntregue = totalEntregue;
	}

	public BigDecimal getPercentual() {
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}
	
	public boolean isValido() {
		return this.percentual != null && this.totalEntregue != null;
	}
	
}
