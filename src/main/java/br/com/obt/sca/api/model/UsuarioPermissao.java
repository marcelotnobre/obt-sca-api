package br.com.obt.sca.api.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.obt.sca.api.model.associativeentity.UsuarioPermissaoPK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
@Table(name = "USUARIO_PERMISSAO")
@EqualsAndHashCode(of = { "usuarioPermissaoPK" })
@ToString(callSuper = true)
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class UsuarioPermissao {

	@EmbeddedId
	private UsuarioPermissaoPK usuarioPermissaoPK;
}
