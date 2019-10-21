package br.com.obt.sca.api.projections.usuario;

import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.obt.sca.api.model.enumeration.TipoAutenticacao;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = { "id" })
@ToString(callSuper = true)
public class UsuarioAndPerfisProjection {

	@ApiModelProperty(notes = "ID gerado pelo banco de dados")
	private Long id;

	@ApiModelProperty(notes = "Login do usuário")
	@NotNull
	@NotEmpty(message = "O campo login é obrigatório!!")
	@Size(min = 3, max = 15, message = "O campo login deve ter entre 5 e 15 caracteres!!")
	private String login;

	@ApiModelProperty(notes = "E-mail do usuário")
	@NotEmpty(message = "O campo e-mail é obrigatório!!")
	@Size(min = 5, max = 50, message = "O campo e-mail deve ter entre 5 e 50 caracteres!!")
	@Email(message = "O campo e-mail deve ter entre 5 e 50 caracteres!!")
	// @Pattern(regexp = ".+@.+\\.[a-z]+", message = "O campo e-mail inválido!")
	private String email;

	@ApiModelProperty(notes = "Senha do usuário")
	@NotEmpty(message = "O campo senha é obrigatório!!")
	private String senha;

	@ApiModelProperty(notes = "Tipo de Autenticação do usuário")
	@Enumerated(EnumType.STRING)
	private TipoAutenticacao tipoAutenticacao;

	@ApiModelProperty(notes = "Status para bloquear ou desbloquear o registro")
	@NotNull(message = "O campo status é obrigatório!")
	private Boolean status;

	private Set<Long> idsPerfis;
}
