package br.com.obt.sca.api.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.obt.sca.api.model.base.BaseEntity;
import br.com.obt.sca.api.model.enumeration.TipoAutenticacao;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
//@formatter:off
@Table(name = "USUARIO", 
	   uniqueConstraints = {
						    @UniqueConstraint(name = "UK_USUARIO_EMAIL", columnNames = {"email"}),
						    @UniqueConstraint(name = "UK_USUARIO_LOGIN", columnNames = {"login"})
	  	    	  		   }, 
	   indexes = {
			      @Index(name = "INDEX_EMAIL_USUARIO", columnList = "email"),
				  @Index(name = "INDEX_LOGIN_USUARIO", columnList = "login"),
				  @Index(name = "INDEX_LOGIN_E_SENHA_USUARIO", columnList = "login,senha"),
				  @Index(name = "INDEX_EMAIL_E_SENHA_USUARIO", columnList = "email,senha")
				 },
	   schema = "sca_api"
       )
@Audited()
@AuditTable(value = "AUDITORIA_USUARIO" , schema = "sca_api_auditoria")
@AuditOverrides(value = {
						  @AuditOverride(forClass = Usuario.class, name = "usuarioPerfis", isAudited = false)
						}
				)
//@formatter:on

@SequenceGenerator(sequenceName = "seq_usuario", name = "ID_SEQUENCE", allocationSize = 1)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = { "login", "senha" })
@ToString(callSuper = true)
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class Usuario extends BaseEntity {

	@Id
	@SequenceGenerator(name = "ID", sequenceName = "seq_usuario", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
	@Column(name = "ID", nullable = false)
	private Long id;

	@ApiModelProperty(notes = "Login do usuário")
	@NotNull(message = "O campo login é obrigatório!!")
	@Size(min = 3, max = 50, message = "O campo login deve ter entre 5 e 15 caracteres!!")
	@Column(name = "LOGIN", length = 50, nullable = false)
	private String login;

	@ApiModelProperty(notes = "E-mail do usuário")
	@NotNull(message = "O campo e-mail é obrigatório!!")
	// @Size(min = 5, max = 50, message = "O campo e-mail deve ter entre 5 e 50
	// caracteres!!")
	@Email(message = "O campo e-mail inválidos!!")
	// @Pattern(regexp = ".+@.+\\.[a-z]+", message = "O campo e-mail inválido!")
	@Column(name = "email", length = 50, nullable = false)
	private String email;

	@ApiModelProperty(notes = "Senha do usuário")
	@NotBlank(message = "O campo senha é obrigatório!!")
	@Column(name = "SENHA", length = 200, nullable = false)
	private String senha;

	@ApiModelProperty(notes = "Tipo de Autenticação do usuário")
	@Column(name = "TIPOAUTENTICACAO", length = 50, nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoAutenticacao tipoAutenticacao;

	@JsonIgnore
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioPerfilPK.usuario")
	@NotAudited
	private Set<UsuarioPerfil> usuarioPerfis;

}
