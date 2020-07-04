package br.com.obt.sca.api.projections;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericoPickListProjection {

	protected Collection<IDAndNomeGenericoProjection> registrosSelecionados;
	protected Collection<IDAndNomeGenericoProjection> registrosDisponiveis;

}
