package br.com.obt.sca.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@formatter:off
@Entity
@Table(name = "ANEXO", uniqueConstraints = {
												@UniqueConstraint(columnNames = { "nome", "tamanho", "tipoConteudo" }, name = "UK_ANEXO_NOME_TAM_TPCONTEUDO") 
										   }
	  )
//@formatter:on
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = { "nome", "tamanho", "tipoConteudo" })
@ToString(callSuper = false, exclude = { "lobAnexo" })
public class Anexo implements Comparable<Anexo> {

	@ApiModelProperty(notes = "ID gerado pelo banco de dados")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Campo nome é obrigatório!")
	@Column(name = "nome", nullable = false)
	private String nome;

	@NotNull(message = "Campo tamanho é obrigatório!")
	@Column(name = "tamanho", nullable = false)
	private Long tamanho;

	@NotBlank(message = "Campo tipo conteudo é obrigatório!")
	@Column(name = "tipoconteudo", nullable = false)
	private String tipoConteudo;

	@NotBlank(message = "Campo label é obrigatório!")
	@Column(name = "label", nullable = false)
	private String label;

	@JsonIgnore
	@Transient
	@Setter(AccessLevel.NONE)
	private LobAnexo lobAnexo;

	public void setLobAnexo(LobAnexo lobAnexo) {
		this.lobAnexo = lobAnexo;
		if (lobAnexo != null) {
			lobAnexo.setAnexo(this);
		}
	}

	@Override
	public int compareTo(Anexo o) {
		return nome.compareTo(o.getNome());
	}
}