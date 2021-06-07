package br.com.obt.sca.api.model.dto;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.obt.sca.api.model.UsuarioPerfil;
import br.com.obt.sca.api.model.enumeration.TipoAutenticacao;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class UsuarioDTO {
	
	private Long id;

	@ApiModelProperty(notes = "Login do usuário")
	private String login;

	@ApiModelProperty(notes = "E-mail do usuário")
	@NotNull(message = "O campo e-mail é obrigatório!!")
	// @Size(min = 5, max = 50, message = "O campo e-mail deve ter entre 5 e 50
	// caracteres!!")
	@Email(message = "O campo e-mail inválidos!!")
	private String email;

	@ApiModelProperty(notes = "Senha do usuário")
	@NotBlank(message = "O campo senha é obrigatório!!")
	private String senha;

	@ApiModelProperty(notes = "Tipo de Autenticação do usuário")
	private TipoAutenticacao tipoAutenticacao;

	@JsonIgnore
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Set<UsuarioPerfil> usuarioPerfis;

	
}
