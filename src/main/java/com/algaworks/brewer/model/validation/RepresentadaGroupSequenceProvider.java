package com.algaworks.brewer.model.validation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import com.algaworks.brewer.model.Representada;

public class RepresentadaGroupSequenceProvider implements DefaultGroupSequenceProvider<Representada> {
	@Override
	public List<Class<?>> getValidationGroups(Representada representada) {
		List<Class<?>> grupos = new ArrayList<>();
		grupos.add(Representada.class);
		
		if (isPessoaSelecionada(representada)) {
			grupos.add(representada.getTipoPessoa().getGrupo());
		}
		
		return grupos;
	}

	private boolean isPessoaSelecionada(Representada representada) {
		return representada != null && representada.getTipoPessoa() != null;
	}
}
