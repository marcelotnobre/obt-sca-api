package br.com.obt.sca.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.obt.sca.api.model.PerfilPermissao;

public interface PerfilPermissaoRepository extends JpaRepository<PerfilPermissao, Long> {

    @Modifying
    @Query("delete from PerfilPermissao PP where PP.perfilPermissaoPK.perfil.id = ?1 and PP.perfilPermissaoPK.permissao.id = ?2")
    void deleteByPerfilPermissao(Long idPerfil, Long idPermissao);

    @Modifying
    @Query(value = "insert into perfil_permissao (perfil_id, permissao_id) values (:idPerfil, :idPermissao) ", nativeQuery = true)
    void savePerfilPermissao(@Param("idPerfil") Long idPerfil, @Param("idPermissao") Long idPermissao);

    @Modifying
    @Query("delete from PerfilPermissao PP where PP.perfilPermissaoPK.perfil.id = ?1 ")
    void deleteByPerfilPermissoes(Long idPerfil);

    @Query("select PP from PerfilPermissao PP where PP.perfilPermissaoPK.perfil.id = ?1 and PP.perfilPermissaoPK.permissao.id = ?2")
    Optional<PerfilPermissao> findByIdPerfilIdPermissao(Long idPerfil, Long idPermissao);

}
