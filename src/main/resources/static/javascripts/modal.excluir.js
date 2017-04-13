Brewer.ComissoesPedido = (function(){
	
	function ComissoesPedido(){
		this.modalExcluir = $('#excluirComissao');
	}
	
	ComissoesPedido.prototype.iniciar = function(){		
		bindExcluir.call(this);
	}
	
	function bindExcluir() {
		var _modal = this.modalExcluir;
		$('.btn-excluir').on('click', function() {
			_modal.find('#codigo').val($(this).data('codigo'));
			_modal.modal('show');
		});
	}
	
	return ComissoesPedido;
	
}());

$(function() {
	var comissao = new Brewer.ComissoesPedido();
	comissao.iniciar();
});