package br.com.obt.sca.api.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.obt.sca.api.model.Perfil;
import java.util.List;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    public Page<Perfil> findByNomeContaining(String nome, Pageable pageable);

    @Query(value = "SELECT P.id, P.nome FROM perfil P where EXISTS  "
            + "  (SELECT UP.* FROM usuario_perfil UP WHERE UP.perfil_id = P.id and UP.usuario_id = :usuarioid ) "
            + " and P.status = true ", nativeQuery = true)
    public <T> Collection<T> findByUsuarioPerfilSelectedStatusTrue(@Param("usuarioid") Long usuarioid, Class<T> type);

    @Query(value = "SELECT P.id, P.nome FROM perfil P where NOT EXISTS  "
            + "  (SELECT UP.* FROM usuario_perfil UP WHERE UP.perfil_id = P.id and UP.usuario_id = :usuarioid ) "
            + " and P.status = true ", nativeQuery = true)
    public <T> Collection<T> findByUsuarioPerfilAvailableStatusTrue(@Param("usuarioid") Long usuarioid, Class<T> type);

    @Query(value = "SELECT * FROM perfil P where EXISTS  "
            + "  (SELECT UP.* FROM usuario_perfil UP WHERE UP.perfil_id = P.id and UP.usuario_id = :usuarioid ) "
            + " and P.status = true ", nativeQuery = true)
    List<Perfil> findByPerfisDoUsuario(@Param("usuarioid") Long usuarioid);

}
