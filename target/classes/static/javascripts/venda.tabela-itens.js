var PedidoItemController = {

	$modalSalvar : undefined,
	$modalExcluir : undefined,

	_populateAndOpenModalSalvar : function(event, item) {
		this.$modalSalvar.find('#codigo').val(item.codigo || '');
		this.$modalSalvar.find('#cod-cerveja').val(item.cerveja.codigo);
		this.$modalSalvar.find('#sku').val(item.cerveja.sku);
		this.$modalSalvar.find('#descricao').val(item.cerveja.descricao || item.cerveja.nome);
		this.$modalSalvar.find('#obs').val(item.observacoes || '');
		this.$modalSalvar.find('#quantidade').val(Brewer.formatarMoeda(item.quantidade));
		this.$modalSalvar.find('#valor').val(Brewer.formatarMoeda(item.valorUnitario));
		this.$modalSalvar.find('#event').val(event);
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
			method : 'POST',
			data : $form.serialize(),
			success : function(html) {
				var event = $form.find('#event').val();
				var codigo = $form.find('#codigo').val();
				TabelaController.trigger(event, [html, codigo]);
				PedidoItemController.$modalSalvar.modal('hide');
			}
		});
	},

	visualizar : function($button) {
		$.ajax({
			url : '/vendas/item',
			method : 'GET',
			dataType : 'json',
			contentType : 'application/json',
			data : {
				codigo : $button.data('codigo')
			},
			success : function(item) {
				PedidoItemController._populateAndOpenModalSalvar('atualizar', item);
			}
		});
	},

	criar : function(item) {
		this._populateAndOpenModalSalvar('adicionar', {
			cerveja : item,
			quantidade : 1,
			valorUnitario : item.valor
		});
	},

	excluir : function($form) {
		$.ajax({
			url : $form.attr('action'),
			method : 'POST',
			data : $form.serialize(),
			success : function(message) {
				PedidoItemController._notify('success', message);
				TabelaController.trigger('excluir', [$form.find('#codigo').val()]);
				PedidoItemController.$modalExcluir.modal('hide');
			}
		});
	},
	
	visualizarExclusao : function($button) {
		var codigo = $button.data('codigo');
		this.$modalExcluir.find('#codigo').val(codigo);
		this.$modalExcluir.find('.codigo').text(codigo);
		this.$modalExcluir.modal('show');
	},

	_initModals : function() {
		function resetForm() {
			$(this).find('form')[0].reset();
		}
		this.$modalSalvar = $('#itemVenda');
		this.$modalExcluir = $('#excluirItem');
		this.$modalSalvar.on('hide.bs.modal', resetForm);
		this.$modalExcluir.on('hide.bs.modal', resetForm);		
	},
	
	_bindFormModal : function($modal, callback) {
		$modal.find('form').on('submit', function(e) {
			e.preventDefault();
			e.stopPropagation();
			callback($(this));
		});
	},
	
	_notify : function(type, message) {
		$('.top-right').notify({ 
			type: type, message: { text: message } 
		}).show();
	},
	
	init : function() {
		TabelaController.init();
		
		var autocomplete = new Brewer.Autocomplete();
		autocomplete.iniciar();
		
		this._initModals();
		
		this._bindFormModal(this.$modalSalvar, PedidoItemController.salvar);
		this._bindFormModal(this.$modalExcluir, PedidoItemController.excluir);		
		
		autocomplete.on('item-selecionado', function(e, item) {
			PedidoItemController.criar(item);
		});
		$('#valor').on('keypress change', function() {
			PedidoItemController._sumAB(this, '#quantidade');
		});
		$('#quantidade').on('keypress change', function() {
			PedidoItemController._sumAB(this, '#valor');
		});
		$('#tabela-itens').on('click', '.btn-alterar', function(e) {
			PedidoItemController.visualizar($(this));
		});
		$('#tabela-itens').on('click', '.btn-excluir', function(e) {
			PedidoItemController.visualizarExclusao($(this));
		});
	}

}

var TabelaController = {
	
	table : '#tabela-itens',
	
	_adicionar : function(event, html) {
		var $tbody = $(TabelaController.table + ' tbody')
		$tbody.find('tr.empty-tr').addClass('hidden');	
		$tbody.append(html);
		TabelaController._refreshTotais();
	},
	
	_atualizar : function(event, html, codigo) {
		var $tr = $(TabelaController.table + ' tr[data-codigo=' + codigo + ']');
		$tr.replaceWith(html);
		TabelaController._refreshTotais();
	},
	
	_excluir : function(event, codigo) {
		var $tbody = $(TabelaController.table + ' tbody');
		$tbody.find('tr[data-codigo=' + codigo + ']').hide('slow', function() {
			$(this).detach();
			TabelaController._refreshTotais();
			if ($tbody.find('tr:not(.empty-tr)').size() == 0) {
				$tbody.find('tr.empty-tr').removeClass('hidden');
			}
		});
	},
	
	_refreshTotais : function() {
		var total = 0;
		$(TabelaController.table + ' tbody td.total-item').each(function() {
			total += Brewer.recuperarValor($(this).text());
		});
		$('.js-valor-total-box').text(Brewer.formatarMoeda(total));
	},
	
	init : function() {
		$.extend(TabelaController, $({}));
		TabelaController.on('atualizar', this._atualizar);
		TabelaController.on('adicionar', this._adicionar);
		TabelaController.on('excluir', this._excluir);
	}
	
}

$(function() {
	PedidoItemController.init();
	
	$('select#status').on('change', function() {
		if ($(this).val() == 'CANCELADA') {
			$('#cervejas #cerveja').attr('disabled', 'disabled');
			$('#tabela-itens .ok').removeClass('hidden');
			$('#tabela-itens .acoes').addClass('hidden');
		} else {
			$('#cervejas #cerveja').removeAttr('disabled');
			$('#tabela-itens .acoes').removeClass('hidden');
			$('#tabela-itens .ok').addClass('hidden');
		}
	});
});
