var CalculaCR = {
	
	entregue : 0,
		
	fields : {
		totalEntregue : '#totalEntregue',
		porcentagemRepresentada : '#porcentagemRepresentada',
		totalPorcentagemRepresentada : '#totalPorcentagemRepresentada',
		porcentagemVendedor : '#porcentagemVendedor',
		totalPorcentagemVendedor : '#totalPorcentagemVendedor'
	},
	
	calcula : function() {
		this.entregue = Brewer.recuperarValor($(this.fields.totalEntregue).val());
		
		var a = Brewer.recuperarValor($(this.fields.porcentagemRepresentada).val());
		this.facaResultado(this.fields.totalPorcentagemRepresentada, a);
		
		var b = Brewer.recuperarValor($(this.fields.porcentagemVendedor).val());
		this.facaResultado(this.fields.totalPorcentagemVendedor, b);
	},
	
	facaResultado : function(field, percent) {
		$(field).val(Brewer.formatarMoeda(this.entregue * (percent / 100)));
	},
	
	init : function() {
		$('.field-calc').on('keyup focusout focusin focus', function() {
			CalculaCR.calcula();
		});
	}
};

CalculaCR.init();