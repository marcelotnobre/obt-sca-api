package br.com.obt.sca.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.obt.sca.api.model.Permissao;
import org.springframework.data.jpa.domain.Specification;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

    public Page<Permissao> findByNomeContainingAndStatusEquals(String nome, Boolean status, Pageable pageable);

    @Query(value = "select P.* from permissao P WHERE P.SISTEMA_ID = :sistemaid and P.nome LIKE %:nome% and P.status =:status", countQuery = "select count(*) FROM permissao P WHERE P.SISTEMA_ID = :sistemaid and P.nome LIKE %:nome% and P.status =:status", nativeQuery = true)
    public Page<Permissao> findByNomeContainingAndSistemaAndStatus(@Param("sistemaid") Long sistemaid,
            @Param("nome") String nome, @Param("status") Boolean status, Pageable pageable);

    @Query(value = "SELECT PE.id, PE.nome FROM permissao PE where exists "
            + " (select id from perfil_permissao where 	  "
            + "  PE.id = perfil_permissao.permissao_id and perfil_permissao.perfil_id = :perfilid ) "
            + " and PE.status = true", nativeQuery = true)
    public <T> Collection<T> findByPermissaoPerfilSelectedStatusTrue(@Param("perfilid") Long perfilid, Class<T> type);

    @Query(value = "SELECT PE.id, PE.nome FROM permissao PE where not exists "
            + " (select id from perfil_permissao where 	  "
            + "  PE.id = perfil_permissao.permissao_id and perfil_permissao.perfil_id = :perfilid ) "
            + " and PE.status = true", nativeQuery = true)
    public <T> Collection<T> findByPermissaoPerfilAvailableStatusTrue(@Param("perfilid") Long perfilid, Class<T> type);

    @Query(value = ""
            + " SELECT permissao.*, perfil.datahorafinalvigencia from permissao"
            + " inner join perfil_permissao on permissao.id = perfil_permissao.permissao_id"
            + " inner join perfil on perfil.id = perfil_permissao.perfil_id"
            + " inner join usuario_perfil on usuario_perfil.perfil_id = perfil.id"
            + " inner join usuario on usuario.id = usuario_perfil.usuario_id"
            + " where 1=1"
            + " and usuario.id = :idUsuario"
            + " and (perfil.datahorafinalvigencia is null or perfil.datahorafinalvigencia >= now())", nativeQuery = true)
    public List<Permissao> findByPermissoesDoUsuario(@Param("idUsuario") Long idUsuario);

    @Query(value = ""
            + "SELECT * from permissao where EXISTS "
            + "("
            + "	select perfil_permissao.permissao_id from usuario_perfil "
            + "	inner join perfil on perfil.id = usuario_perfil.perfil_id "
            + "	inner join perfil_permissao on (usuario_perfil.perfil_id =perfil_permissao.perfil_id) "
            + "	where perfil_permissao.permissao_id = permissao.id and (perfil.id = :idPerfil) "
            + ")", nativeQuery = true)
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
    public List<Permissao> findPermissoes(@Param("nomePermissao") String nomePermissao, @Param("idUsuario") Long idUsuario);

    @Query(value = "select P.* from permissao P WHERE P.SISTEMA_ID = :idSistema and P.nome = :nomePermissao", nativeQuery = true)
    Permissao findPermissaoPorSistema(@Param("nomePermissao") String nomePermissao, @Param("idSistema") Long idSistema);

    public Page<Permissao> findAll(Specification<Permissao> spec, Pageable pageable);

    public Long count(Specification<Permissao> spec);

}
