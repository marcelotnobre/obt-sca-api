package br.com.obt.sca.api.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.obt.sca.api.model.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
//@formatter:off
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
@Table(name = "PERMISSAO", 
	   uniqueConstraints = {
			   				 @UniqueConstraint(name = "UK_PERMISSAO_NOME", columnNames = {"nome"})},
       indexes = {
                  @Index(name = "INDEX_NOME_PERMISSAO", columnList = "nome")
				 }
)
//@formatter:on
@SequenceGenerator(sequenceName = "seq_permissao", name = "ID_SEQUENCE", allocationSize = 1)

//@AssociationOverrides({
//				  	  @AssociationOverride(joinColumns=@JoinColumn(name = "USUARIO_CADASTRO_ID"),name = "usuarioCadastro", foreignKey = @ForeignKey(name="FK_PERMISSAO_USUARIOCADASTRO")),
//					  @AssociationOverride(joinColumns=@JoinColumn(name = "USUARIO_ALTERACAO_ID"),name = "usuarioAlteracao", foreignKey = @ForeignKey(name="FK_PERMISSAO_USUARIOALTERACAO"))
//					 })

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = { "nome" })
@ToString(callSuper = true)
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class Permissao extends BaseEntity {

	@Id
	@SequenceGenerator(name = "ID", sequenceName = "seq_permissao", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_permissao")
	@Column(name = "ID", nullable = false)
	private Long id;

	@ApiModelProperty(notes = "Nome da permissão - deve seguir a seguinte nomeclatura ROLE_ACAO_NOMECLASSEMAIUSCULA. Ex.:  ROLE_CRUD_CATEGORIA")
	@NotNull(message = "O campo nome é obrigatório!")
	@Size(min = 3, max = 100, message = "O campo nome deve ter o tamanho entre {min} e {max} caractere.")
	@Column(name = "nome", nullable = false)
	private String nome;

	@ApiModelProperty(notes = "Descrição da permissão - Default : repetir o nome da permissão")
	@NotNull(message = "O campo descrição é obrigatório!")
	@Size(min = 3, max = 500, message = "O campo descrição deve ter o tamanho entre {min} e {max}  caractere.")
	@Column(name = "DESCRICAO", length = 500, nullable = false)
	private String descricao;

	@ApiModelProperty(notes = "Sistema vinculado a permissão")
	@NotNull(message = "O campo sistema é obrigatório!")
	@ManyToOne(fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "SISTEMA_ID", foreignKey = @ForeignKey(name = "FK_PERMISSAO_SISTEMA"))
	private Sistema sistema;

	@JsonIgnore
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "perfilPermissaoPK.permissao")
	private List<PerfilPermissao> perfilPermissoes;

}
