package br.com.obt.sca.api.model.associativeentity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.obt.sca.api.model.Permissao;
import br.com.obt.sca.api.model.Usuario;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"usuario", "permissao"})
@Embeddable
public class UsuarioPermissaoPK implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "USUARIO_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_USUARIOPERMISSAO_USUARIO"), referencedColumnName = "ID")
    private Usuario usuario;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "PERMISSAO_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_USUARIOPERMISSAO_PERMISSAO"), referencedColumnName = "ID")
    private Permissao permissao;

}
