package br.com.obt.sca.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.obt.sca.api.model.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
//@formatter:off
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
@Table(name = "SISTEMA", uniqueConstraints = { 
												@UniqueConstraint(name = "UK_SISTEMA_NOME", columnNames = { "nome" } ) 
											 } 
	  )
//@formatter:on
@SequenceGenerator(sequenceName = "seq_sistema", name = "ID_SEQUENCE", allocationSize = 1)

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = { "nome" })
@ToString(callSuper = true)
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class Sistema extends BaseEntity {

	@Id
	@SequenceGenerator(name = "ID", sequenceName = "seq_sistema", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sistema")
	@Column(name = "ID", nullable = false)
	private Long id;

	@ApiModelProperty(notes = "Nome do sistema")
	@NotBlank(message = "O campo nome é obrigatório!")
	@Size(min = 3, max = 100, message = "O campo nome deve ter o tamanho entre {min} e {max} caractere.")
	@Column(name = "nome")
	private String nome;

	@ApiModelProperty(notes = "Descrição do sistema")
	@NotBlank(message = "O campo descrição é obrigatório!")
	@Size(min = 3, max = 500, message = "O campo descrição deve ter o tamanho entre {min} e {max} caractere.")
	@Column(name = "DESCRICAO", length = 500)
	private String descricao;

	@ApiModelProperty(notes = "URL da api do sistema")
	@NotBlank(message = "O campo URL API é obrigatório!")
	@Pattern(regexp = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", message = "URL inválida.")
	@Column(name = "URLAPI", length = 200)
	private String urlAPI;

	@ApiModelProperty(notes = "URL do projeto web")
	@NotBlank(message = "O campo URL WEB é obrigatório!")
	@Pattern(regexp = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", message = "URL inválida.")
	@Column(name = "URLWEB", length = 200)
	private String urlWEB;

	@ApiModelProperty(notes = "URL da logo do sistema")
	@NotBlank(message = "O campo URL WEB é obrigatório!")
	@Column(name = "URLLOGO", length = 200)
	private String urlLogo;

}
