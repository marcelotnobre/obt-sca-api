package br.com.obt.sca.api.model.associativeentity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.obt.sca.api.model.Sistema;
import br.com.obt.sca.api.model.Usuario;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = { "usuario", "sistema" })
@Embeddable
public class UsuarioSistemaPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "USUARIO_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_USUARIOSISTEMA_USUARIO"), referencedColumnName = "ID")
	private Usuario usuario;

	@ManyToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "SISTEMA_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_USUARIOPERFIL_SISTEMA"), referencedColumnName = "ID")
	private Sistema sistema;

}
