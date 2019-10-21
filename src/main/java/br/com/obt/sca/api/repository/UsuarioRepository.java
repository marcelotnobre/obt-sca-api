package br.com.obt.sca.api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.obt.sca.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	//@formatter:off

//	@Query("SELECT U FROM Usuario U INNER JOIN fetch U.pessoa P where ((U.login = ?1 or U.email = ?1) and (U.status = true and  P.status=true))")
	@Query("SELECT U FROM Usuario U where ((U.login = ?1 or U.email = ?1) and (U.status = true ))")
	public Optional<Usuario> findByEmailOrLogin(String emailOrLogin);

	@Query("SELECT U FROM Usuario U where (U.login like %?1%)")
	public Page<Usuario> findByNomeContaining(String login, Pageable pageable);

	
	
	
	//@formatter:on

}
