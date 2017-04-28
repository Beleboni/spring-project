//Brewer.TabelaItens = (function() {
//
//	// Pegando as classes necessarias
//	function TabelaItens(autocomplete) {
//		this.autocomplete = autocomplete;
//		this.tabelaCervejasContainer = $('.js-tabela-cervejas-container');
//		this.uuid = $('#uuid').val();
//		this.emitter = $({});
//		this.on = this.emitter.on.bind(this.emitter);
//		this.modal = $('#itemVenda');
//		this.modalExcluir = $('#excluirItem');
//	}
//
//	// Iniciando os modais
//	TabelaItens.prototype.iniciar = function() {
//		this.autocomplete.on('item-selecionado', onItemSelecionado.bind(this));
//
//		this.modal.find('form').on('submit', salvarItemSelecionado.bind(this));
//		this.modal.on('hide.bs.modal', function() {
//			$(this).find('form')[0].reset();
//		});
//
//		this.modalExcluir.find('form').on('submit',
//				excluirItemSelecionado.bind(this));
//		this.modalExcluir.on('hide.bs.modal', function() {
//			$(this).find('form')[0].reset();
//		});
//
//		bindAlterar.call(this);
//		bindExcluir.call(this);
//		bindTabelaItem.call(this);
//	}
//
//	// Montando o valor total da tabela
//	TabelaItens.prototype.valorTotal = function() {
//		return this.tabelaCervejasContainer.data('valor');
//	}
//
//	// Salva ou altera um item selecionado.
//	function salvarItemSelecionado(evento) {
//		evento.preventDefault();
//		evento.stopPropagation();
//
//		var _form = this.modal.find('form');
//		var resposta = $.ajax({
//			url : _form.attr('action'),
//			method : _form.attr('method'),
//			data : this.modal.find('form').serialize()
//		});
//
//		resposta.done(onItemAtualizadoNoServidor.bind(this));
//	}
//
//	// Excluir item da tabela
//	function excluirItemSelecionado(evento) {
//		evento.preventDefault();
//		evento.stopPropagation();
//
//		var _form = this.modalExcluir.find('form');
//		var resposta = $.ajax({
//			url : 'item/' + this.uuid + '/excluir/'
//					+ _form.find('#uuidItem').val(),
//			method : 'delete'
//		});
//
//		resposta.done(onItemAtualizadoNoServidor.bind(this));
//	}
//
//	// Abre modal com os dados do item.
//	function onItemSelecionado(evento, item) {
//		populateAndOpenModalSalvar(this.modal, {
//			cerveja : item,
//			quantidade : 1,
//			valorUnitario : item.valor
//		});
//	}
//
//	// Atualiza os itens no servidor
//	function onItemAtualizadoNoServidor(html) {
//		this.tabelaCervejasContainer.html(html);
//
//		this.modal.modal('hide');
//		this.modalExcluir.modal('hide');
//
//		bindAlterar.call(this);
//		bindExcluir.call(this);
//
//		var tabelaItem = bindTabelaItem.call(this);
//		this.emitter.trigger('tabela-itens-atualizada', tabelaItem
//				.data('valor-total'));
//	}
//
//	// Populando o modal de alterar
//	function bindAlterar() {
//		var _modal = this.modal;
//		$('.btn-alterar').on('click', function(e) {
//			var _this = $(this);
//			$.ajax({
//				url : '/vendas/item',
//				dataType : 'json',
//				contentType : 'application/json',
//				type : 'GET',
//				data : {
//					codigo : _this.data('codigo')
//				},
//				success : function(item) {
//					populateAndOpenModalSalvar(_modal, item);
//				}
//			});
//		});
//	}
//
//	// Populando o modal de excluir
//	function bindExcluir() {
//		var _modal = this.modalExcluir;
//		$('.btn-excluir').on('click', function() {
//			var _tr = $(this).closest('tr');
//			_modal.find('#uuidItem').val($(this).data('uuid'))
//			_modal.find('#descricao').val(_tr.find('.descricao').text());
//			_modal.modal('show');
//		});
//	}
//
//	// Bind
//	function bindTabelaItem() {
//		var tabelaItem = $('.js-tabela-item');
//		return tabelaItem;
//	}
//
//	return TabelaItens;
//
//}());
//
//function populateAndOpenModalSalvar(modal, item) {
//	modal.find('#codigo').val(item.codigo || '');
//	modal.find('#cod-cerveja').val(item.cerveja.codigo);
//	modal.find('#sku').val(item.cerveja.sku);
//	modal.find('#descricao').val(item.cerveja.descricao || item.cerveja.nome);
//	modal.find('#obs').val(item.observacoes || '');
//	modal.find('#quantidade').val(item.quantidade);
//	modal.find('#valor').val(item.valorUnitario);
//	sumAB('#quantidade', '#valor');
//	modal.modal({
//		'backdrop' : 'static',
//		'keyboard' : false
//	});
//}

var PedidoItemController = {

	$modalSalvar : undefined,
	$modalExcluir : undefined,

	_populateAndOpenModalSalvar : function(item) {
		this.$modalSalvar.find('#codigo').val(item.codigo || '');
		this.$modalSalvar.find('#cod-cerveja').val(item.cerveja.codigo);
		this.$modalSalvar.find('#sku').val(item.cerveja.sku);
		this.$modalSalvar.find('#descricao').val(item.cerveja.descricao || item.cerveja.nome);
		this.$modalSalvar.find('#obs').val(item.observacoes || '');
		this.$modalSalvar.find('#quantidade').val(Brewer.formatarMoeda(item.quantidade));
		this.$modalSalvar.find('#valor').val(Brewer.formatarMoeda(item.valorUnitario));
		this._sumAB('#quantidade', '#valor');
		this.$modalSalvar.modal({
			'backdrop' : 'static',
			'keyboard' : false
		});
	},

	_sumAB : function(idA, idB) {
		var a = Brewer.recuperarValor($(idA).val());
		var b = Brewer.recuperarValor($(idB).val());
		$('#rs_total').val(Brewer.formatarMoeda(a * b));
	},

	salvar : function($form) {
		$.ajax({
			url : $form.attr('action'),
			method : 'post',
			data : $form.serialize(),
			success : function(item) {
				// lógica de adição na tabela.
				// compilar o template.
				PedidoItemController.$modalSalvar.modal('hide');
			}
		});
	},

	visualizar : function($button) {
		$.ajax({
			url : '/vendas/item',
			dataType : 'json',
			contentType : 'application/json',
			type : 'GET',
			data : {
				codigo : $button.data('codigo')
			},
			success : function(item) {
				PedidoItemController._populateAndOpenModalSalvar(item);
			}
		});
	},

	criar : function(item) {
		this._populateAndOpenModalSalvar({
			cerveja : item,
			quantidade : 1,
			valorUnitario : item.valor
		});
	},

	excluir : function($form) {
		$.ajax({
			url : '/vendas/item/excluir',
			dataType : 'json',
			contentType : 'application/json',
			type : 'DELETE',
			data : $form.serialize(),
			success : function(message) {
				// remove item da tabela e mosta mensagem
			}
		});
	},
	
	visualizarExclusao : function($button) {
		this.$modalExcluir.find('#codigo').val($button.data('codigo'));
		this.$modalExcluir.modal('show');
	},

	_createModals : function() {
		this.$modalSalvar = $('#itemVenda');
		this.$modalExcluir = $('#excluirItem');
	},
	
	init : function(autocomplete) {
		this._createModals();
		
		this.$modalSalvar.on('hide.bs.modal', function() {
			$(this).find('form')[0].reset();
		});
		this.$modalExcluir.on('hide.bs.modal', function() {
			$(this).find('form')[0].reset();
		});
		
		this.$modalSalvar.find('form').on('submit', function(e) {
			e.preventDefault();
			e.stopPropagation();
			PedidoItemController.salvar($(this));
		});
		$('#valor').on('keypress change', function() {
			PedidoItemController._sumAB(this, '#quantidade');
		});
		$('#quantidade').on('keypress change', function() {
			PedidoItemController._sumAB(this, '#valor');
		});
		autocomplete.on('item-selecionado', function(e, item) {
			PedidoItemController.criar(item);
		});
		$('.btn-alterar').on('click', function(e) {
			PedidoItemController.visualizar($(this));
		});
		$('.btn-excluir').on('click', function() {
			PedidoItemController.visualizarExclusao($(this));
		});
	}

}

$(function() {
	var autocomplete = new Brewer.Autocomplete();
	autocomplete.iniciar();
	PedidoItemController.init(autocomplete);
});
