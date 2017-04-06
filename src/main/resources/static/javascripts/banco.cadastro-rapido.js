var Brewer = Brewer || {};

Brewer.BancoCadastroRapido = (function() {
	
	function BancoCadastroRapido() {
		this.modal = $('#modalCadastroRapidoBanco');
		this.botaoSalvar = this.modal.find('.js-modal-cadastro-banco-salvar-btn');
		this.form = this.modal.find('form');
		this.url = this.form.attr('action');
		this.inputNomeBanco = $('#nomeBanco');
		this.containerMensagemErro = $('.js-mensagem-cadastro-rapido-banco');
	}
	
	BancoCadastroRapido.prototype.iniciar = function() {
		this.form.on('submit', function(event) { event.preventDefault() });
		this.modal.on('shown.bs.modal', onModalShow.bind(this));
		this.modal.on('hide.bs.modal', onModalClose.bind(this))
		this.botaoSalvar.on('click', onBotaoSalvarClick.bind(this));
	}
	
	function onModalShow() {
		this.inputNomeBanco.focus();
	}
	
	function onModalClose() {
		this.inputNomeBanco.val('');
		this.containerMensagemErro.addClass('hidden');
		this.form.find('.form-group').removeClass('has-error');
	}
	
	function onBotaoSalvarClick() {
		var nomeBanco = this.inputNomeBanco.val().trim();
		$.ajax({
			url: this.url,
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({ descricao: nomeBanco }),
			error: onErroSalvandoBanco.bind(this),
			success: onBancoSalvo.bind(this)
		});
	}
	
	function onErroSalvandoBanco(obj) {
		var mensagemErro = obj.responseText;
		this.containerMensagemErro.removeClass('hidden');
		this.containerMensagemErro.html('<span>' + mensagemErro + '</span>');
		this.form.find('.form-group').addClass('has-error');
	}
	
	function onBancoSalvo(banco) {
		var comboBanco = $('#banco');
		comboBanco.append('<option value=' + banco.codigo + '>' + banco.descricao + '</option>');
		comboBanco.val(banco.codigo);
		this.modal.modal('hide');
	}
	
	return BancoCadastroRapido;
	
}());

$(function() {
	var bancoCadastroRapido = new Brewer.BancoCadastroRapido();
	bancoCadastroRapido.iniciar();
});
