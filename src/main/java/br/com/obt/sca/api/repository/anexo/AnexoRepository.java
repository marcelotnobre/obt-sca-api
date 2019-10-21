package br.com.obt.sca.api.repository.anexo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.obt.sca.api.model.Anexo;

@Repository
public interface AnexoRepository extends JpaRepository<Anexo, Long> {

}
