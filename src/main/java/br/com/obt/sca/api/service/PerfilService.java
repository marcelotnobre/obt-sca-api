package br.com.obt.sca.api.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.obt.sca.api.model.Perfil;
import br.com.obt.sca.api.projections.GenericoPinkListProjection;
import br.com.obt.sca.api.projections.IDAndNomeGenericoProjection;
import br.com.obt.sca.api.projections.perfil.PerfilAndPermissoesProjection;
import br.com.obt.sca.api.repository.PerfilRepository;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ResourceParameterNullException;
import br.com.obt.sca.api.service.exception.ServiceException;

//@formatter:off
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { ServiceException.class })
//@formatter:on
@Service
public class PerfilService {

	// private static final Logger logger =
	// LoggerFactory.getLogger(PerfilService.class);

	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private PerfilPermissaoService perfilPermissaoService;

	// @formatter:off
	@Transactional(readOnly = false)
	public PerfilAndPermissoesProjection savePerfilAndPermissions(
			PerfilAndPermissoesProjection perfilAndPermissoesProjection)
			throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

		Perfil perfil = new Perfil();
		BeanUtils.copyProperties(perfilAndPermissoesProjection, perfil, "idsPermissoes");
		Perfil perfilSalvo = save(perfil);

		perfilPermissaoService.savePerfilPermissionsIDs(perfilSalvo, perfilAndPermissoesProjection.getIdsPermissoes());

		return perfilAndPermissoesProjection;

	}

	@Transactional(readOnly = false)
	public Perfil save(Perfil perfil) throws ResourceAlreadyExistsException, ResourceNotFoundException {

		if ((perfil != null) && (perfil.getId() != null)) {
			Optional<Perfil> perfilBanco = this.findById(perfil.getId());
			// Setando os campos que são alterados na tela.
			perfilBanco.get().setNome(perfil.getNome());
			perfilBanco.get().setDescricao(perfil.getDescricao());
			perfilBanco.get().setSistema(perfil.getSistema());
			perfilBanco.get().setDataHoraFinalVigencia(perfil.getDataHoraFinalVigencia());
			perfilBanco.get().setStatus(perfil.getStatus());

			BeanUtils.copyProperties(perfilBanco.get(), perfil, "id");
		}

		if (perfil != null) {
			return perfilRepository.save(perfil);
		}

		return perfil;

	}
	// @formatter:on

	@Transactional(readOnly = false)
	public void updatePropertyStatus(Long id, Boolean status) throws ResourceNotFoundException {
		Optional<Perfil> perfilBanco = findById(id);

		Perfil perfil = perfilBanco.get();
		perfil.setStatus(status);
		perfilRepository.save(perfil);
	}

	@Transactional(readOnly = false)
	public void deleteById(Long id) throws ResourceNotFoundException {
		validatefindByIdExists(id);
		perfilRepository.deleteById(id);

	}

	public Page<Perfil> findByNomeContaining(String nome, Pageable pageable) {
		return perfilRepository.findByNomeContaining(nome, pageable);
	}

	public Page<Perfil> findByAll(Pageable pageable) {
		return perfilRepository.findAll(pageable);
	}

	public Optional<Perfil> findById(Long id) throws ResourceNotFoundException {
		Optional<Perfil> perfilBanco = perfilRepository.findById(id);
		if (!perfilBanco.isPresent()) {
			throw new ResourceNotFoundException("O código " + id + " do perfil não foi encontrado. ");
		}
		return perfilBanco;
	}

	public Collection<IDAndNomeGenericoProjection> findByUsuarioPerfilAvailableStatusTrue(Long usuarioid) {
		return perfilRepository.findByUsuarioPerfilAvailableStatusTrue(usuarioid, IDAndNomeGenericoProjection.class);
	}

	public Collection<IDAndNomeGenericoProjection> findByUsuarioPerfilSelectedStatusTrue(Long usuarioid) {
		return perfilRepository.findByUsuarioPerfilSelectedStatusTrue(usuarioid, IDAndNomeGenericoProjection.class);
	}

	public GenericoPinkListProjection findByPerfilPinkListProjection(Long perfilid) {

		GenericoPinkListProjection perfisPinkListProjection = new GenericoPinkListProjection();

		Collection<IDAndNomeGenericoProjection> perfisSelecionadas = this
				.findByUsuarioPerfilSelectedStatusTrue(perfilid);
		Collection<IDAndNomeGenericoProjection> perfisDisponiveis = this
				.findByUsuarioPerfilAvailableStatusTrue(perfilid);

		perfisPinkListProjection.setRegistrosDisponiveis(perfisDisponiveis);
		perfisPinkListProjection.setRegistrosSelecionados(perfisSelecionadas);

		return perfisPinkListProjection;
	}

	// Metodos Privados
	private void validatefindByIdExists(Long id) throws ResourceNotFoundException {
		Optional<Perfil> perfilBanco = this.findById(id);
		if (!perfilBanco.isPresent()) {
			throw new ResourceNotFoundException("O código " + id + " do perfil não foi encontrado. ");
		}
	}

}
