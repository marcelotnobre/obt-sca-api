package br.com.obt.sca.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.obt.sca.api.model.Sistema;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface SistemaRepository extends JpaRepository<Sistema, Long> {

    public Page<Sistema> findByNomeContainingAndStatusEquals(String nome, Boolean status, Pageable pageable);

    Sistema findByNomeEquals(String nome);

    public List<Sistema> findByStatusTrue();

    <T> Collection<T> findByStatusTrue(Class<T> type);

    @Query(value = "SELECT S.id, S.nome FROM sistema S where exists " + "(select id from usuario_sistema US where "
            + " S.id = US.sistema_id and US.usuario_id = :usuarioID ) " + "and S.status = true", nativeQuery = true)
    public <T> Collection<T> findBySistemaUsuarioSelectedStatusTrue(@Param("usuarioID") Long usuarioID, Class<T> type);

    @Query(value = "SELECT S.id, S.nome FROM sistema S where not exists " + "(select id from usuario_sistema US where "
            + " S.id = US.sistema_id and US.usuario_id = :usuarioID ) " + "and S.status = true", nativeQuery = true)
    public <T> Collection<T> findBySistemaUsuarioAvailableStatusTrue(@Param("usuarioID") Long usuarioID, Class<T> type);

    @Query(value = "SELECT S.id, S.nome, S.descricao, S.URLAPI, S.URLWEB, S.URLLOGO, S.status FROM sistema S where exists " + "(select id from usuario_sistema US where "
            + " S.id = US.sistema_id and US.usuario_id = :usuarioID ) " + "and S.status = true", nativeQuery = true)
    public List<Sistema> findAllByUsuario(@Param("usuarioID") Long usuarioID);

    Page<Sistema> findAll(Specification<Sistema> spec, Pageable pageable);

    Long count(Specification<Sistema> spec);

    @Transactional
    @Modifying
    @Query("update Sistema S set S.status = :status where S.id = :idSistema")
    void updateStatusSistema(@Param("idSistema") Long idSistema, @Param("status") boolean status);
}
