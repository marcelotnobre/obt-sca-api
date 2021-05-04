package br.com.obt.sca.api.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import br.com.obt.sca.api.model.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
//@formatter:off
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
@Table(name = "PERFIL", 
       uniqueConstraints = { 
		  				    @UniqueConstraint(name = "UK_PERFIL_NOME" , columnNames = { "nome" } ) 
						   }
	   )
//@formatter:on
@SequenceGenerator(sequenceName = "seq_perfil", name = "ID_SEQUENCE", allocationSize = 1)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = { "id" })
@ToString(callSuper = true)
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class Perfil extends BaseEntity {

	@Id
	@SequenceGenerator(name = "ID", sequenceName = "seq_perfil", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_perfil")
	@Column(name = "ID", nullable = false)
	private Long id;

	@ApiModelProperty(notes = "Nome do perfil")
	@NotBlank(message = "Campo nome é obrigatório!")
	@Size(min = 3, max = 200, message = "O campo descrição deve ter o tamanho entre {min} e {max} caractere. ")
	@Column(name = "NOME", length = 200)
	private String nome;

	@ApiModelProperty(notes = "Descrição da perfil - Default : repetir o nome do perfil")
	@NotBlank(message = "Campo descrição é obrigatório!")
	@Size(min = 3, max = 1000, message = "O campo descrição deve ter o tamanho entre {min} e {max} caractere. ")
	@Column(name = "DESCRICAO", length = 1000)
	private String descricao;

	@NotNull(message = "Campo sistema é obrigatório!")
	@ManyToOne(fetch = FetchType.LAZY)
	// @Fetch(FetchMode.JOIN)
	@JoinColumn(name = "SISTEMA_ID", foreignKey = @ForeignKey(name = "FK_PERFIL_SISTEMA"))
	private Sistema sistema;

	@ApiModelProperty(notes = "Data e hora final de vigência")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@Column(name = "DATAHORAFINALVIGENCIA")
	private LocalDateTime dataHoraFinalVigencia;

	@JsonIgnore
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioPerfilPK.perfil")
	private Set<UsuarioPerfil> usuarioPerfis;

	@JsonIgnore
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "perfilPermissaoPK.perfil")
	private Set<PerfilPermissao> perfilPermissoes;

}
