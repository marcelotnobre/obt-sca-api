package br.com.obt.sca.api.model.base;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Endereco {

	@ApiModelProperty(notes = "Logradouro")
	// @NotBlank(message = "O campo logradouro é obrigatório!")
	@Column(name = "LOGRADOURO", length = 200)
	private String logradouro;

	@ApiModelProperty(notes = "Numero do endereço")
	// @NotBlank(message = "O campo número é obrigatório!")
	@Column(name = "NUMERO", length = 50)
	private String numero;

	@ApiModelProperty(notes = "Complemento do endereço")
	// @NotBlank(message = "O campo complemento é obrigatório!")
	@Column(name = "COMPLEMENTO", length = 200)
	private String complemento;

	@ApiModelProperty(notes = "Bairro")
	// @NotBlank(message = "O campo bairro é obrigatório!")
	@Column(name = "BAIRRO", length = 100)
	private String bairro;

	@ApiModelProperty(notes = "Cep")
	// @NotBlank(message = "O campo cep é obrigatório!")
	@Column(name = "CEP", length = 20)
	private String cep;

	@ApiModelProperty(notes = "Cidade")
	// @NotBlank(message = "O campo cidade é obrigatório!")
	@Column(name = "CIDADE", length = 200)
	private String cidade;

	@ApiModelProperty(notes = "Estado")
	// @NotBlank(message = "O campo estado é obrigatório!")
	@Column(name = "ESTADO", length = 50)
	private String estado;

}
