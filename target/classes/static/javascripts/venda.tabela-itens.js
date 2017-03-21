Brewer.TabelaItens = (function() {
	
	function TabelaItens(autocomplete) {
		this.autocomplete = autocomplete;
		this.tabelaCervejasContainer = $('.js-tabela-cervejas-container');
		this.uuid = $('#uuid').val();
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
		this.modal = $('#itemVenda');
	}
	
	TabelaItens.prototype.iniciar = function() {
		this.autocomplete.on('item-selecionado', onItemSelecionado.bind(this));
		
		this.modal.find('form').on('submit', salvarItemSelecionado.bind(this));
		this.modal.on('hide.bs.modal', function() {
			$(this).find('form')[0].reset();
		});
		bindAlterar.call(this);
		bindTabelaItem.call(this);
	}
	
	TabelaItens.prototype.valorTotal = function() {
		return this.tabelaCervejasContainer.data('valor');
	}
	
	// Salva ou altera um item selecionado.
	function salvarItemSelecionado(evento) {		
		evento.preventDefault();
		evento.stopPropagation();
		
		var _form = this.modal.find('form');
		var resposta = $.ajax({
			url: _form.attr('action'),
			method: _form.attr('method'), 
			data: this.modal.find('form').serialize()
		});	
		
		resposta.done(onItemAtualizadoNoServidor.bind(this));
	}
	
	// Abre modal com os dados do item.
	function onItemSelecionado(evento, item) {
		this.modal.find('form').attr('action', 'item/' + item.codigo + '/adicionar');
		this.modal.find('form').attr('method', 'post');
		this.modal.find('#codigo').val(item.codigo);
		this.modal.find('#sku').val(item.sku);
		this.modal.find('#descricao').val(item.nome);
		this.modal.find('#valor').val(item.valor);
		this.modal.modal('show');
	}
	
	function onItemAtualizadoNoServidor(html) {
		this.tabelaCervejasContainer.html(html);
		this.modal.modal('hide');
		
		bindAlterar.call(this);
		
		var tabelaItem = bindTabelaItem.call(this); 
		this.emitter.trigger('tabela-itens-atualizada', tabelaItem.data('valor-total'));
	}
	
	function onQuantidadeItemAlterado(evento) {
		var input = $(evento.target);
		var quantidade = input.val();
		
		if (quantidade <= 0) {
			input.val(1);
			quantidade = 1;
		}
		
		var codigoCerveja = input.data('codigo-cerveja');
		
		var resposta = $.ajax({
			url: 'item/' + codigoCerveja,
			method: 'PUT',
			data: {
				quantidade: quantidade,
				uuid: this.uuid
			}
		});
		
		resposta.done(onItemAtualizadoNoServidor.bind(this));
	}
	
	function onValorItemAlterado(evento) {
		var input = $(evento.target);
		var valor = input.val();
		
		if (valor <= 0) {
			input.val(1);
			valor = 1;
		}
		
		var codigoCerveja = input.data('codigo-cerveja');
		
		var resposta = $.ajax({
			url: 'item/' + codigoCerveja,
			method: 'PUT',
			data: {
				valor: valor,
				uuid: this.uuid
			}
		});
		
		resposta.done(onItemAtualizadoNoServidor.bind(this));
	}
	
	function onObservacaoItemAlterado(evento) {
		var input = $(evento.target);
		
		var resposta = $.ajax({
			url : 'item/' + input.data('codigo-cerveja'),
			method : 'PUT',
			data : {
				observacao : input.val(),
				uuid : this.uuid
			}
		});
		
		resposta.done(onItemAtualizadoNoServidor.bind(this));
	}
	
	function onDoubleClick(evento) {
		$(this).toggleClass('solicitando-exclusao');
	}
	
	function onExclusaoItemClick(evento) {
		var codigoCerveja = $(evento.target).data('codigo-cerveja');
		var resposta = $.ajax({
			url: 'item/' + this.uuid + '/' + codigoCerveja,
			method: 'DELETE'
		});
		
		resposta.done(onItemAtualizadoNoServidor.bind(this));
	}
	
	function bindQuantidade() {
		var quantidadeItemInput = $('.js-tabela-cerveja-quantidade-item');
		quantidadeItemInput.on('focusout', onQuantidadeItemAlterado.bind(this));
		quantidadeItemInput.maskNumber({ integer: true, thousands: '' });
	}
	
	function bindValor() {
		var valorItemInput = $('.js-tabela-cerveja-valor-item');
		valorItemInput.on('focusout', onValorItemAlterado.bind(this));
		// Aqui
		valorItemInput.maskNumber({ decimal: '.', thousands: '' });
		// daqui
	}
	
	function bindObservacao() {
		var obsItemInput = $('.js-tabela-cerveja-observacao-item');
		obsItemInput.on('change', onObservacaoItemAlterado.bind(this));
	}
	
	function bindTabelaItem() {
		var tabelaItem = $('.js-tabela-item');
		tabelaItem.on('dblclick', onDoubleClick);
		$('.js-exclusao-item-btn').on('click', onExclusaoItemClick.bind(this));
		return tabelaItem;
	}
	
	function bindAlterar() {
		var _modal = this.modal;
		$('.btn-alterar').on('click', function() {
			var _tr = $(this).closest('tr');
			_modal.find('form').attr('action', 'item/' + $(this).data('codigo') + '/alterar');
			_modal.find('form').attr('method', 'put');
			_modal.find('#uuidItem').val($(this).data('uuid'));
			_modal.find('#codigo').val($(this).data('codigo'));
			_modal.find('#sku').val(_tr.find('.sku').text());
			_modal.find('#descricao').val(_tr.find('.descricao').text());
			_modal.find('#obs').val(_tr.find('.observacao').text());
			_modal.find('#quantidade').val(_tr.find('.quantidade').text().trim());
			_modal.find('#valor').val(Brewer.recuperarValor(_tr.find('.valor').text()));
			_modal.modal('show');
		});
	}  
	
	return TabelaItens;
	
}());
