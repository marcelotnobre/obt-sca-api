package br.com.obt.sca.api.projections.usuario;

import java.util.Set;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"id"})
@ToString(callSuper = true)
public class UsuarioAndPermissaoProjection {

    @ApiModelProperty(notes = "ID gerado pelo banco de dados")
    private Long id;

    private Set<Long> idsPermissoes;
}
