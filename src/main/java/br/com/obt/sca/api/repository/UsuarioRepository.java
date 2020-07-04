package br.com.obt.sca.api.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import br.com.obt.sca.api.model.Usuario;
import br.com.obt.sca.api.repository.superclass.GenericRepository;

public interface UsuarioRepository extends GenericRepository<Usuario, Long> {

//	@Query("SELECT U FROM Usuario U INNER JOIN fetch U.pessoa P where ((U.login = ?1 or U.email = ?1) and (U.status = true and  P.status=true))")
    @Query("SELECT U FROM Usuario U where ((U.login = ?1 or U.email = ?1) and (U.status = true ))")
    public Optional<Usuario> findByEmailOrLogin(String emailOrLogin);

    @Query("SELECT U FROM Usuario U where (U.login like %?1%)")
    public Page<Usuario> findByNomeContaining(String login, Pageable pageable);

    @Query("SELECT U FROM Usuario U where (U.login like %?1% AND U.email like %?2%)")
    public Page<Usuario> findByEmailAndLogin(String login, String email, Pageable pageable);

    @Query("SELECT U FROM Usuario U where (U.login like %?1% or U.email like %?1%)")
    public Page<Usuario> findByEmailOrLogin(String emailOrLogin, Pageable pageable);

    @Query("SELECT U FROM Usuario U where (U.login = ?1 or U.email = ?1)")
    public Optional<Usuario> findByLogin(String login);

}
