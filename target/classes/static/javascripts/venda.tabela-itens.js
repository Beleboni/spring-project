Brewer.TabelaItens = (function() {
	
	//Pegando as classes necessarias
	function TabelaItens(autocomplete) {
		this.autocomplete = autocomplete;
		this.tabelaCervejasContainer = $('.js-tabela-cervejas-container');
		this.uuid = $('#uuid').val();
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
		this.modal = $('#itemVenda');
		this.modalExcluir = $('#excluirItem');
	}
	
	//Iniciando os modais
	TabelaItens.prototype.iniciar = function() {
		this.autocomplete.on('item-selecionado', onItemSelecionado.bind(this));
		
		this.modal.find('form').on('submit', salvarItemSelecionado.bind(this));
		this.modal.on('hide.bs.modal', function() {
			$(this).find('form')[0].reset();
		});
		
		this.modalExcluir.find('form').on('submit', excluirItemSelecionado.bind(this));
		this.modalExcluir.on('hide.bs.modal', function() {
			$(this).find('form')[0].reset();
		});
		
		
		bindAlterar.call(this);
		bindExcluir.call(this);
		bindTabelaItem.call(this);
	}
	
	//Montando o valor total da tabela
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
	
	// Excluir item da tabela
	function excluirItemSelecionado(evento){
		evento.preventDefault();
		evento.stopPropagation();
		
		var _form = this.modalExcluir.find('form');
		var resposta = $.ajax({
			url: 'item/' + this.uuid + '/excluir/' + _form.find('#uuidItem').val(),
			method: 'delete'	
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
		this.modal.find('#quantidade').val(Brewer.formatarMoeda(1));
		this.modal.find('#valor').val(Brewer.formatarMoeda(item.valor));
		this.modal.modal({'backdrop': 'static', 'keyboard': false});
		sumAB('#quantidade', '#valor');
	}
	
	//Atualiza os itens no servidor
	function onItemAtualizadoNoServidor(html) {
		this.tabelaCervejasContainer.html(html);
		
		this.modal.modal('hide');
		this.modalExcluir.modal('hide');
		
		bindAlterar.call(this);
		bindExcluir.call(this);
		
		var tabelaItem = bindTabelaItem.call(this); 
		this.emitter.trigger('tabela-itens-atualizada', tabelaItem.data('valor-total'));
	}
	
	//Populando o modal de alterar
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
			_modal.modal({'backdrop': 'static', 'keyboard': false});
			sumAB('#quantidade', '#valor');
		});
	}
	
	//Populando o modal de excluir
	function bindExcluir(){
		var _modal = this.modalExcluir;
		$('.btn-excluir').on('click', function() {
				var _tr = $(this).closest('tr');
				_modal.find('#uuidItem').val($(this).data('uuid'))
				_modal.find('#descricao').val(_tr.find('.descricao').text());
				_modal.modal('show');
		});
	}
	
	//Bind
	function bindTabelaItem() {
		var tabelaItem = $('.js-tabela-item');
		return tabelaItem;
	}
		
	return TabelaItens;
	
}());


function sumAB(idA, idB) {
	var a = Brewer.recuperarValor($(idA).val());
	var b = Brewer.recuperarValor($(idB).val());
	$('#rs_total').val(Brewer.formatarMoeda(a * b));
}

$('#valor').on('keypress change', function() {
	sumAB(this, '#quantidade');
});

$('#quantidade').on('keypress change', function() {
	sumAB(this, '#valor');
});
