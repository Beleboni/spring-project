package com.algaworks.brewer.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name = "venda")
public class Venda {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao = LocalDateTime.now();

	@Column(name = "data_finalizacao")
	private LocalDateTime dataFinalizacao;

	private String observacao;

	@Column(name = "data_hora_entrega")
	private LocalDateTime dataHoraEntrega;

	@ManyToOne
	@JoinColumn(name = "codigo_cliente")
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "codigo_banco")
	private Banco banco;

	@ManyToOne
	@JoinColumn(name = "codigo_usuario")
	private Usuario usuario;

	@Enumerated(EnumType.STRING)
	private StatusVenda status = StatusVenda.ORCAMENTO;

	@OneToMany(mappedBy = "venda", cascade = CascadeType.REMOVE)
	private List<ItemVenda> itens = new ArrayList<>();

	@Column(name = "valor_total")
	private BigDecimal valorTotal;

	public Venda(Long codigo) {
		this.codigo = codigo;
	}

	public Venda() {
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

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public LocalDateTime getDataHoraEntrega() {
		return dataHoraEntrega;
	}

	public void setDataHoraEntrega(LocalDateTime dataHoraEntrega) {
		this.dataHoraEntrega = dataHoraEntrega;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public StatusVenda getStatus() {
		return status;
	}

	public void setStatus(StatusVenda status) {
		this.status = status;
	}

	public List<ItemVenda> getItens() {
		return itens;
	}

	public void setItens(List<ItemVenda> itens) {
		this.itens = itens;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public boolean isNova() {
		return codigo == null;
	}

	public BigDecimal getValorTotalItens() {
		return getItens().stream().map(ItemVenda::getValorTotal)
				.reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	}

	public Long getDiasCriacao() {
		LocalDate inicio = dataCriacao != null ? dataCriacao.toLocalDate()
				: LocalDate.now();
		return ChronoUnit.DAYS.between(inicio, LocalDate.now());
	}

	public boolean isSalvarPermitido() {
		return StatusVenda.ORCAMENTO.equals(this.status);
	}

	public boolean isSalvarProibido() {
		return !isSalvarPermitido();
	}

	public boolean isPodeAlterarStatus() {
		return Arrays.asList(StatusVenda.EMITIDA,
				StatusVenda.ENTREGUE_PARCIALMENTE).contains(this.status);
	}

	public boolean isNaoCadastraComissao() {
		return Arrays.asList(StatusVenda.CANCELADA, StatusVenda.ORCAMENTO,
				StatusVenda.EMITIDA, StatusVenda.CONCLUIDO).contains(
				this.status);
	}

	public LocalDateTime getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(LocalDateTime dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public boolean isPermiteEdicaoItens() {
		return Arrays.asList(StatusVenda.EMITIDA, StatusVenda.ORCAMENTO).contains(this.status);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Venda other = (Venda) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
