package br.com.obt.sca.api.dto;

import java.util.List;

import br.com.obt.sca.api.model.Perfil;
import br.com.obt.sca.api.model.Permissao;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilPermissoesListDTO {

	protected Perfil perfil;
	protected List<Permissao> permissoes;

}
