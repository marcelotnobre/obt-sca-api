package br.com.obt.sca.api.repository.anexo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.obt.sca.api.model.LobAnexo;

@Repository
public interface LobAnexoRepository extends JpaRepository<LobAnexo, Long> {

}
