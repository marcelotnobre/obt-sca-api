package br.com.obt.sca.api.model.audit;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import br.com.obt.sca.api.util.DataUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@RevisionEntity(AuditRevisionListener.class)
@Table(name = "AUDITORIA")
@AttributeOverrides({ @AttributeOverride(name = "timestamp", column = @Column(name = "REVTSTMP")),
		@AttributeOverride(name = "id", column = @Column(name = "REV")) })
@Getter
@Setter
@ToString
public class AuditRevisionEntity extends DefaultRevisionEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "USUARIO_LOGIN", nullable = false)
	private String usuarioLogin;

	@Column(name = "USUARIO_EMAIL", nullable = false)
	private String usuarioEmail;

	@Column(name = "IP", nullable = true)
	public String ip;

	@Column(name = "HOST_NAME", nullable = true)
	public String hostName;

	public String getDataHora() {
		LocalDateTime dataHora = new Timestamp(this.getTimestamp()).toLocalDateTime();
		return DataUtils.getDataHoraFormatada(dataHora, "dd/MM/yyyy HH:mm:ss");
	}

}