package com.algaworks.brewer.controller.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.algaworks.brewer.model.Pedido;

@Component
public class PedidoValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Pedido.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "cliente.codigo", "", "Selecione um cliente na pesquisa rápida");
		
		Pedido pedido = (Pedido) target;
		validarSeInformouItens(errors, pedido);
		validarValorTotalNegativo(errors, pedido);
	}

	private void validarValorTotalNegativo(Errors errors, Pedido pedido) {
		if (pedido.getValorTotal().compareTo(BigDecimal.ZERO) < 0) {
			errors.reject("", "Valor total não pode ser negativo");
		}
	}

	private void validarSeInformouItens(Errors errors, Pedido pedido) {
		if (pedido.getItens().isEmpty()) {
			errors.reject("", "Adicione pelo menos um  produto pedido");
		}
	}

}
