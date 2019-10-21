package br.com.obt.sca.api.model.base;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * IdLong e Status
 */
@Getter
@Setter
@EqualsAndHashCode(of = { "id" })
@MappedSuperclass
public abstract class BaseEntity {

	@ApiModelProperty(notes = "ID gerado pelo banco de dados")
	@Id
	// Essa linha só será usada em banco postgres
	// @GeneratedValue(strategy = GenerationType.IDENTITY, generator =
	// "ID_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "Status para bloquear ou desbloquear o registro")
	@NotNull(message = "O campo status é obrigatório!")
	@Column(name = "STATUS", nullable = false)
	private Boolean status;

}
