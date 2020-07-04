package br.com.obt.sca.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.obt.sca.api.model.UsuarioSistema;

public interface UsuarioSistemaRepository extends JpaRepository<UsuarioSistema, Long> {

    @Modifying
    @Query("delete from UsuarioSistema US where US.usuarioSistemaPK.usuario.id = ?1 and US.usuarioSistemaPK.sistema.id = ?2")
    void deleteByUsuarioSistema(Long idUsuario, Long idSistema);

    @Modifying
    @Query(value = "insert into usuario_sistema (usuario_id, sistema_id) values (:idUsuario, :idSistema) ", nativeQuery = true)
    void saveUsuarioSistema(@Param("idUsuario") Long idUsuario, @Param("idSistema") Long idSistema);

    @Modifying
    @Query("delete from UsuarioSistema US where US.usuarioSistemaPK.usuario.id = ?1 ")
    void deleteByUsuarioSistemas(Long idUsuario);

    @Query("select US from UsuarioSistema US where US.usuarioSistemaPK.usuario.id = ?1 and US.usuarioSistemaPK.sistema.id = ?2")
    public Optional<UsuarioSistema> findByIdUsuarioSistemalId(Long idUsuario, Long idSistema);

}
