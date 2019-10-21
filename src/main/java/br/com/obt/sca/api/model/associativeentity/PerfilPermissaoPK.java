package br.com.obt.sca.api.model.associativeentity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.obt.sca.api.model.Perfil;
import br.com.obt.sca.api.model.Permissao;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = { "perfil", "permissao" })
@Embeddable
public class PerfilPermissaoPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "PERFIL_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PERFILPERMISSAO_PERFIL"))
	private Perfil perfil;

	@ManyToOne(fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "PERMISSAO_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PERFILPERMISSAO_PERMISSAO"))
	private Permissao permissao;

}
