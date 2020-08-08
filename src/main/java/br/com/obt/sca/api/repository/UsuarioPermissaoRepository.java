package br.com.obt.sca.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.obt.sca.api.model.UsuarioPermissao;
import java.util.List;

public interface UsuarioPermissaoRepository extends JpaRepository<UsuarioPermissao, Long> {

    @Modifying
    @Query("delete from UsuarioPermissao UP where UP.usuarioPermissaoPK.usuario.id = ?1 and UP.usuarioPermissaoPK.permissao.id = ?2")
    void deleteByUsuarioPermissao(Long idUsuario, Long idPermissao);

    @Modifying
    @Query(value = "delete from usuario_permissao where usuario_id = :idUsuario and permissao_id IN (:idPermissoes)", nativeQuery = true)
    void deleteByUsuarioPermissao(Long idUsuario, List<Long> idPermissoes);

    @Modifying
    @Query(value = "insert into usuario_permissao (usuario_id, permissao_id) values (:idUsuario, :idPermissao) ", nativeQuery = true)
    void saveUsuarioPermissao(@Param("idUsuario") Long idUsuario, @Param("idPermissao") Long idPermissao);

    @Modifying
    @Query("delete from UsuarioPermissao UP where UP.usuarioPermissaoPK.usuario.id = ?1 ")
    void deleteByPermissaoPermissoes(Long idUsuario);

    @Query("select UP from UsuarioPermissao UP where UP.usuarioPermissaoPK.usuario.id = ?1 and UP.usuarioPermissaoPK.permissao.id = ?2")
    Optional<UsuarioPermissao> findByIdUsuarioPermissaoId(Long idUsuario, Long idPermissao);

}
