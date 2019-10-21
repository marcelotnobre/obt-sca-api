package br.com.obt.sca.api.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.obt.sca.api.model.associativeentity.PerfilPermissaoPK;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
// @Cacheable(value = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
@Table(name = "PERFIL_PERMISSAO")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
// @JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class,
// property = "@id", scope = PerfilPermissao.class)
public class PerfilPermissao {

	@EmbeddedId
	private PerfilPermissaoPK perfilPermissaoPK;

	// @ApiModelProperty(notes = "Data e hora do cadastro do registro")
	// @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	// @JsonSerialize(using = LocalDateTimeSerializer.class)
	// @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	// @Column(name = "DATAHORACADASTRO")
	// private LocalDateTime dataHoraCadastro;

	// @ApiModelProperty(notes = "Data e hora da alteração do registro")
	// @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	// @JsonSerialize(using = LocalDateTimeSerializer.class)
	// @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	// @Column(name = "DATAHORAALTERACAO")
	// private LocalDateTime dataHoraAlteracao;

	// @PrePersist
	// private void initTimeStamps() {
	// if (dataHoraCadastro == null) {
	// setDataHoraCadastro(LocalDateTime.now());
	// }
	// setDataHoraAlteracao(LocalDateTime.now());
	// }

	// @PreUpdate
	// private void updateTimeStamp() {
	// setDataHoraAlteracao(LocalDateTime.now());
	// }
}
