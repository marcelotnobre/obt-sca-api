package br.com.obt.sca.api.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(exclude = { "lob" }, callSuper = true)
@EqualsAndHashCode(of = "anexo", callSuper = false)
public class LobAnexo {

	@Id
	@GeneratedValue(generator = "foreign")
	@GenericGenerator(name = "foreign", strategy = "foreign", parameters = {
			@Parameter(name = "property", value = "anexo") })
	private Long id;

	@Lob
	@Column(nullable = false)
	private byte[] lob;

	@JsonIgnore
	@OneToOne(optional = false, cascade = CascadeType.REMOVE)
	@PrimaryKeyJoinColumn
	private Anexo anexo;

	public LobAnexo() {
	}

	public LobAnexo(byte[] newLob) {
		if (newLob != null) {
			newLob = newLob.clone();
		}

		this.lob = newLob;
	}

}
