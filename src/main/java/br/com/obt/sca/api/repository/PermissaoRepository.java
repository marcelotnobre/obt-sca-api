package br.com.obt.sca.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.obt.sca.api.model.Permissao;
import br.com.obt.sca.api.repository.superclass.GenericRepository;

public interface PermissaoRepository extends GenericRepository<Permissao, Long> {

    Page<Permissao> findByNomeContainingAndStatusEquals(String nome, Boolean status, Pageable pageable);

    @Query(value = "select P.* from permissao P WHERE P.SISTEMA_ID = :sistemaid and P.nome LIKE %:nome% and P.status =:status", countQuery = "select count(*) FROM permissao P WHERE P.SISTEMA_ID = :sistemaid and P.nome LIKE %:nome% and P.status =:status", nativeQuery = true)
    Page<Permissao> findByNomeContainingAndSistemaAndStatus(@Param("sistemaid") Long sistemaid,
            @Param("nome") String nome, @Param("status") Boolean status, Pageable pageable);

    @Query(value = "SELECT PE.id, PE.nome FROM permissao PE where exists "
            + " (select id from perfil_permissao where 	  "
            + "  PE.id = perfil_permissao.permissao_id and perfil_permissao.perfil_id = :perfilid ) "
            + " and PE.status = true", nativeQuery = true)
    <T> Collection<T> findByPermissaoPerfilSelectedStatusTrue(@Param("perfilid") Long perfilid, Class<T> type);

    @Query(value = "SELECT PE.id, PE.nome FROM permissao PE where not exists "
            + " (select id from perfil_permissao where 	  "
            + "  PE.id = perfil_permissao.permissao_id and perfil_permissao.perfil_id = :perfilid ) "
            + " and PE.status = true", nativeQuery = true)
    <T> Collection<T> findByPermissaoPerfilAvailableStatusTrue(@Param("perfilid") Long perfilid, Class<T> type);

    @Query(value = ""
            + " SELECT distinct permissao.*, perfil.datahorafinalvigencia from permissao"
            + " inner join perfil_permissao on permissao.id = perfil_permissao.permissao_id"
            + " inner join perfil on perfil.id = perfil_permissao.perfil_id"
            + " inner join usuario_perfil on usuario_perfil.perfil_id = perfil.id"
            + " inner join usuario on usuario.id = usuario_perfil.usuario_id"
            + " where 1=1"
            + " and usuario.id = :idUsuario"
            + " and (perfil.datahorafinalvigencia is null or perfil.datahorafinalvigencia >= now())", nativeQuery = true)
    List<Permissao> findByUsuarioJoinUsuarioPerfil(@Param("idUsuario") Long idUsuario);

    @Query(value = "SELECT P.id, P.nome FROM permissao P where 1=1 and P.status = true", nativeQuery = true)
    <T> Collection<T> findByPermissaoAvailableStatusTrue(Class<T> type);

    @Query(value = ""
            + " SELECT distinct permissao.* from permissao"
            + " inner join usuario_permissao on usuario_permissao.permissao_id = permissao.id"
            + " inner join usuario on usuario_permissao.usuario_id = usuario.id"
            + " where 1=1"
            + " and usuario.id = :idUsuario", nativeQuery = true)
    List<Permissao> findByUsuarioJoinUsuarioPermissao(@Param("idUsuario") Long idUsuario);

    @Query(value = ""
            + " SELECT distinct permissao.* from permissao"
            + " inner join usuario_permissao on usuario_permissao.permissao_id = permissao.id"
            + " inner join usuario on usuario_permissao.usuario_id = usuario.id"
            + " where usuario_permissao.usuario_id = :idUsuario", nativeQuery = true)
    <T> Collection<T> findSelecionadasByUsuarioJoinUsuarioPermissao(@Param("idUsuario") Long idUsuario, Class<T> type);

    @Query(value = ""
            + " SELECT P.id, P.nome FROM permissao P where"
            + " P.status = true", nativeQuery = true)
    <T> Collection<T> findDisponiveisByNovoUsuario(Class<T> type);
    
    @Query(value = ""
            + " SELECT P.id, P.nome FROM permissao P where not exists"
            + " (select id from usuario_permissao UP where"
            + " P.id = UP.permissao_id and UP.usuario_id = :idUsuario )"
            + " and P.id NOT IN (:idPermissoes) "
            + " and P.status = true", nativeQuery = true)
    <T> Collection<T> findDisponiveisByUsuarioJoinUsuarioPermissao(@Param("idUsuario") Long idUsuario,
            @Param("idPermissoes") List<Long> idPermissoes, Class<T> type);

    @Query(value = ""
            + " SELECT permissao.* FROM permissao"
            + " INNER JOIN perfil_permissao ON permissao.id = perfil_permissao.permissao_id"
            + " WHERE 1=1"
            + " AND permissao.status = true"
            + " AND perfil_permissao.perfil_id = :idPerfil", nativeQuery = true)
    List<Permissao> findByPermissoesDoPerfil(@Param("idPerfil") Long idPerfil);

    @Query(value = ""
            + " select permissao.* from permissao"
            + " inner join perfil_permissao on perfil_permissao.permissao_id = permissao.id"
            + " inner join usuario_perfil on usuario_perfil.perfil_id = perfil_permissao.perfil_id"
            + " inner join perfil on usuario_perfil.perfil_id = perfil.id"
            + " where 1=1"
            + " and usuario_perfil.usuario_id = :idUsuario"
            + " and permissao.nome = :nomePermissao"
            + " and (perfil.datahorafinalvigencia is null or perfil.datahorafinalvigencia >= now())", nativeQuery = true)
    List<Permissao> findPermissoes(@Param("nomePermissao") String nomePermissao, @Param("idUsuario") Long idUsuario);

    @Query(value = "select P.* from permissao P WHERE P.SISTEMA_ID = :idSistema and P.nome = :nomePermissao", nativeQuery = true)
    Permissao findPermissaoPorSistema(@Param("nomePermissao") String nomePermissao, @Param("idSistema") Long idSistema);

}
