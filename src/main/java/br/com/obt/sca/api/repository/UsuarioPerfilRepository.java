package br.com.obt.sca.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.obt.sca.api.model.UsuarioPerfil;

public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, Long> {

	@Modifying
	@Query("delete from UsuarioPerfil UP where UP.usuarioPerfilPK.usuario.id = ?1 and UP.usuarioPerfilPK.perfil.id = ?2")
	void deleteByUsuarioPerfil(Long idUsuario, Long idPerfil);

	@Modifying
	@Query(value = "insert into usuario_perfil (usuario_id, perfil_id) values (:idUsuario, :idPerfil) ", nativeQuery = true)
	void saveUsuarioPerfil(@Param("idUsuario") Long idUsuario, @Param("idPerfil") Long idPerfil);

	@Modifying
	@Query("delete from UsuarioPerfil UP where UP.usuarioPerfilPK.usuario.id = ?1 ")
	void deleteByPerfilPermissoes(Long idUsuario);

	@Query("select UP from UsuarioPerfil UP where UP.usuarioPerfilPK.usuario.id = ?1 and UP.usuarioPerfilPK.perfil.id = ?2")
	public Optional<UsuarioPerfil> findByIdUsuarioPerfilId(Long idUsuario, Long idPerfil);

}
