package br.com.obt.sca.api.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.obt.sca.api.model.Perfil;
import br.com.obt.sca.api.repository.superclass.GenericRepository;
import java.util.List;

public interface PerfilRepository extends GenericRepository<Perfil, Long> {

    Page<Perfil> findByNomeContaining(String nome, Pageable pageable);

    @Query(value = "SELECT P.id, P.nome FROM perfil P where EXISTS  "
            + "  (SELECT UP.* FROM usuario_perfil UP WHERE UP.perfil_id = P.id and UP.usuario_id = :usuarioid ) "
            + " and P.status = true and (P.datahorafinalvigencia is null or P.datahorafinalvigencia >= now()) ", nativeQuery = true)
    <T> Collection<T> findByUsuarioPerfilSelectedStatusTrue(@Param("usuarioid") Long usuarioid,
            Class<T> type);

    @Query(value = "SELECT distinct P.id, P.nome FROM perfil P where EXISTS  "
            + "  (SELECT UP.* FROM usuario_perfil UP WHERE UP.perfil_id = P.id and UP.usuario_id = :usuarioid ) "
            + " and P.sistema_id IN (:sistemas) "
            + " and P.status = true and (P.datahorafinalvigencia is null or P.datahorafinalvigencia >= now()) ", nativeQuery = true)
    <T> Collection<T> findByUsuarioPerfilSistemaSelectedStatusTrue(@Param("usuarioid") Long usuarioid,
            @Param("sistemas") List<Long> sistemas, Class<T> type);

    @Query(value = "SELECT distinct P.id, P.nome FROM perfil P where NOT EXISTS  "
            + "  (SELECT UP.* FROM usuario_perfil UP WHERE UP.perfil_id = P.id and UP.usuario_id = :usuarioid ) "
            + " and P.sistema_id IN(:sistemas) "
            + " and P.status = true and (P.datahorafinalvigencia is null or P.datahorafinalvigencia >= now()) ", nativeQuery = true)
    <T> Collection<T> findByUsuarioPerfilSistemaAvailableStatusTrue(@Param("usuarioid") Long usuarioid,
            @Param("sistemas") List<Long> sistemas, Class<T> type);

    // FOR PICKLIST
    @Query(value = "SELECT distinct P.id, concat(sistema.nome, ' - ', P.nome) as nome FROM perfil P join sistema "
            + " where EXISTS (SELECT UP.* FROM usuario_perfil UP WHERE UP.perfil_id = P.id and UP.usuario_id = :usuarioid ) "
            + " and P.sistema_id IN (:sistemas) "
            + " and P.status = true and (P.datahorafinalvigencia is null or P.datahorafinalvigencia >= now()) ", nativeQuery = true)
    <T> Collection<T> findByUsuarioPerfilSistemaSelectedStatusTrueForPicklist(
            @Param("usuarioid") Long usuarioid, @Param("sistemas") List<Long> sistemas, Class<T> type);

    // FOR PICKLIST
    @Query(value = "SELECT distinct P.id, concat(sistema.nome, ' - ', P.nome) as nome FROM perfil P join sistema "
            + " where  NOT EXISTS  (SELECT UP.* FROM usuario_perfil UP WHERE UP.perfil_id = P.id and UP.usuario_id = :usuarioid ) "
            + " and P.sistema_id IN(:sistemas) "
            + " and P.status = true and (P.datahorafinalvigencia is null or P.datahorafinalvigencia >= now()) ", nativeQuery = true)
    <T> Collection<T> findByUsuarioPerfilSistemaAvailableStatusTrueForPicklist(
            @Param("usuarioid") Long usuarioid, @Param("sistemas") List<Long> sistemas, Class<T> type);

    @Query(value = "SELECT P.id, P.nome FROM perfil P where NOT EXISTS  "
            + "  (SELECT UP.* FROM usuario_perfil UP WHERE UP.perfil_id = P.id and UP.usuario_id = :usuarioid ) "
            + " and P.status = true and (P.datahorafinalvigencia is null or P.datahorafinalvigencia >= now()) ", nativeQuery = true)
    <T> Collection<T> findByUsuarioPerfilAvailableStatusTrue(@Param("usuarioid") Long usuarioid,
            Class<T> type);

    @Query(value = "SELECT * FROM perfil P where EXISTS  "
            + "  (SELECT UP.* FROM usuario_perfil UP WHERE UP.perfil_id = P.id and UP.usuario_id = :usuarioid ) "
            + " and P.status = true and (P.datahorafinalvigencia is null or P.datahorafinalvigencia >= now()) ", nativeQuery = true)
    List<Perfil> findByPerfisDoUsuario(@Param("usuarioid") Long usuarioid);

}
