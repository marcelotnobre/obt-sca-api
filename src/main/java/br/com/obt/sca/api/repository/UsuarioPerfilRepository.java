package br.com.obt.sca.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.obt.sca.api.model.UsuarioPerfil;
import java.util.List;
import java.util.Set;

public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, Long> {

    @Modifying
    @Query("delete from UsuarioPerfil UP where UP.usuarioPerfilPK.usuario.id = ?1 and UP.usuarioPerfilPK.perfil.id = ?2")
    void deleteByUsuarioPerfil(Long idUsuario, Long idPerfil);

    @Modifying
    @Query(value = "delete from usuario_perfil where usuario_id = :idUsuario and perfil_id IN (:idPerfis)", nativeQuery = true)
    void deleteByUsuarioPerfil(Long idUsuario, List<Long> idPerfis);

    @Query(value = ""
            + " select distinct usuario_perfil.* from usuario_perfil "
            + " inner join perfil on usuario_perfil.perfil_id = perfil.id "
            + " where 1=1 "
            + " and usuario_perfil.usuario_id = :idUsuario "
            + " and perfil.sistema_id NOT IN (:idSistemas) ", nativeQuery = true)
    List<Long> findByUsuarioSistema(@Param("idUsuario") Long idUsuario,
            @Param("idSistemas") Set<Long> idSistemas);

    @Modifying
    @Query(value = "insert into usuario_perfil (usuario_id, perfil_id) values (:idUsuario, :idPerfil) ", nativeQuery = true)
    void saveUsuarioPerfil(@Param("idUsuario") Long idUsuario, @Param("idPerfil") Long idPerfil);

    @Modifying
    @Query("delete from UsuarioPerfil UP where UP.usuarioPerfilPK.usuario.id = ?1 ")
    void deleteByPerfilPermissoes(Long idUsuario);

    @Query("select UP from UsuarioPerfil UP where UP.usuarioPerfilPK.usuario.id = ?1 and UP.usuarioPerfilPK.perfil.id = ?2")
    Optional<UsuarioPerfil> findByIdUsuarioPerfilId(Long idUsuario, Long idPerfil);

}
