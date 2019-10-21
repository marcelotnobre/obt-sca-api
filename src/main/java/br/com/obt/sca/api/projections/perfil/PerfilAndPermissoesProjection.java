package br.com.obt.sca.api.projections.perfil;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import br.com.obt.sca.api.model.Sistema;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = { "id" })
@ToString(callSuper = true)
public class PerfilAndPermissoesProjection {

	@ApiModelProperty(notes = "ID gerado pelo banco de dados")
	private Long id;

	@ApiModelProperty(notes = "Nome do perfil")
	@NotBlank(message = "Campo nome é obrigatório!")
	@Size(min = 3, max = 200, message = "O campo descrição deve ter o tamanho entre {min} e {max} caractere. ")
	private String nome;

	@ApiModelProperty(notes = "Descrição da perfil - Default : repetir o nome do perfil")
	@NotBlank(message = "Campo descrição é obrigatório!")
	@Size(min = 3, max = 1000, message = "O campo descrição deve ter o tamanho entre {min} e {max} caractere. ")
	private String descricao;

	@NotNull(message = "Campo sistema é obrigatório!")
	private Sistema sistema;

	@ApiModelProperty(notes = "Data e hora final de vigência")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@Column(name = "DATAHORAFINALVIGENCIA")
	private LocalDateTime dataHoraFinalVigencia;

	@ApiModelProperty(notes = "Status para bloquear ou desbloquear o registro")
	@NotNull(message = "O campo status é obrigatório!")
	private Boolean status;

	private Set<Long> idsPermissoes;
}
