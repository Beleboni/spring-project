Brewer = Brewer || {};

Brewer.PesquisaRapidaUsuario = (function() {
	
	function PesquisaRapidaUsuario() {
		this.pesquisaRapidaUsuariosModal = $('#pesquisaRapidaUsuarios');
		this.nomeInput = $('#nomeUsuarioModal');
		this.pesquisaRapidaBtn = $('.js-pesquisa-rapida-usuarios-btn'); 
		this.containerTabelaPesquisa = $('#containerTabelaPesquisaRapidaUsuarios');
		this.htmlTabelaPesquisa = $('#tabela-pesquisa-rapida-usuario').html();
		this.template = Handlebars.compile(this.htmlTabelaPesquisa);
		this.mensagemErro = $('.js-mensagem-erro-usuario');
	}
	
	PesquisaRapidaUsuario.prototype.iniciar = function() {
		this.pesquisaRapidaBtn.on('click', onPesquisaRapidaClicado.bind(this));
		this.pesquisaRapidaUsuariosModal.on('shown.bs.modal', onModalShow.bind(this));

	}
	
	function onModalShow() {
		this.nomeInput.focus();
	}
	
	function onPesquisaRapidaClicado(event) {
		event.preventDefault();
		
		$.ajax({
			url: this.pesquisaRapidaUsuariosModal.find('form').attr('action'),
			method: 'GET',
			contentType: 'application/json',
			data: {
				nome: this.nomeInput.val()
			}, 
			success: onPesquisaConcluida.bind(this),
			error: onErroPesquisa.bind(this)
		});
	}
	
	function onPesquisaConcluida(resultado) {
		this.mensagemErro.addClass('hidden');
		
		var html = this.template(resultado);
		this.containerTabelaPesquisa.html(html);
		
		var tabelaUsuarioPesquisaRapida = new Brewer.TabelaUsuarioPesquisaRapida(this.pesquisaRapidaUsuariosModal);
		tabelaUsuarioPesquisaRapida.iniciar();
	} 
	
	function onErroPesquisa() {
		this.mensagemErro.removeClass('hidden');
	}
	
	return PesquisaRapidaUsuario;
	
}());

Brewer.TabelaUsuarioPesquisaRapida = (function() {
	
	function TabelaUsuarioPesquisaRapida(modal) {
		this.modalUsuario = modal;
		this.usuario = $('.js-usuario-pesquisa-rapida');
	}
	
	TabelaUsuarioPesquisaRapida.prototype.iniciar = function() {
		this.usuario.on('click', onUsuarioSelecionado.bind(this));
	}
	
	function onUsuarioSelecionado(evento) {
		this.modalUsuario.modal('hide');
		
		var usuarioSelecionado = $(evento.currentTarget);
		$('#nomeUsuario').val(usuarioSelecionado.data('nome'));
		$('#codigoUsuario').val(usuarioSelecionado.data('codigo'));
	}
	
	return TabelaUsuarioPesquisaRapida;
	
}());

$(function() {
	var pesquisaRapidaUsuario = new Brewer.PesquisaRapidaUsuario();
	pesquisaRapidaUsuario.iniciar();
});