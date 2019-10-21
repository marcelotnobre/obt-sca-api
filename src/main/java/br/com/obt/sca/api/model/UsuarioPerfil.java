package br.com.obt.sca.api.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.obt.sca.api.model.associativeentity.UsuarioPerfilPK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
// @Cacheable(value = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
@Table(name = "USUARIO_PERFIL")
@EqualsAndHashCode(of = { "usuarioPerfilPK" })
@ToString(callSuper = true)
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class UsuarioPerfil {

	@EmbeddedId
	private UsuarioPerfilPK usuarioPerfilPK;
}
