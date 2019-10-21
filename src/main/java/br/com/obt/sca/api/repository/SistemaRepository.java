package br.com.obt.sca.api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.obt.sca.api.model.Sistema;

public interface SistemaRepository extends JpaRepository<Sistema, Long> {

	public Page<Sistema> findByNomeContainingAndStatusEquals(String nome, Boolean status, Pageable pageable);

	public List<Sistema> findByStatusTrue();

	<T> Collection<T> findByStatusTrue(Class<T> type);

	@Query(value = "SELECT S.id, S.nome FROM sistema S where exists " + "(select id from usuario_sistema US where "
			+ " S.id = US.sistema_id and US.usuario_id = :usuarioID ) " + "and S.status = true", nativeQuery = true)
	public <T> Collection<T> findBySistemaUsuarioSelectedStatusTrue(@Param("usuarioID") Long usuarioID, Class<T> type);

	@Query(value = "SELECT S.id, S.nome FROM sistema S where not exists " + "(select id from usuario_sistema US where "
			+ " S.id = US.sistema_id and US.usuario_id = :usuarioID ) " + "and S.status = true", nativeQuery = true)
	public <T> Collection<T> findBySistemaUsuarioAvailableStatusTrue(@Param("usuarioID") Long usuarioID, Class<T> type);

}
