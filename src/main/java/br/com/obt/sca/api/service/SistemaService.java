package br.com.obt.sca.api.service;

import br.com.obt.sca.api.model.Sistema;
import br.com.obt.sca.api.repository.SistemaRepository;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import br.com.obt.sca.api.projections.GenericoPinkListProjection;
import br.com.obt.sca.api.projections.IDAndNomeGenericoProjection;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ServiceException;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

//@formatter:off
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
//@formatter:on
@Service
public class SistemaService {

    // private static final Logger logger =
    // LoggerFactory.getLogger(SistemaService.class);
    @Autowired
    private SistemaRepository sistemaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional(readOnly = false)
    public Sistema save(Sistema sistema) throws ResourceAlreadyExistsException, ResourceNotFoundException {

        if ((sistema != null) && (sistema.getId() != null)) {
            Optional<Sistema> sistemaBanco = this.findById(sistema.getId());
            // Setando os campos que são alterados na tela.
            sistemaBanco.get().setNome(sistema.getNome());
            sistemaBanco.get().setDescricao(sistema.getDescricao());
            sistemaBanco.get().setStatus(sistema.getStatus());
            sistemaBanco.get().setUrlAPI(sistema.getUrlAPI());
            sistemaBanco.get().setUrlWEB(sistema.getUrlWEB());

            BeanUtils.copyProperties(sistemaBanco.get(), sistema, "id");
        }

        if (sistema != null) {
            return sistemaRepository.save(sistema);
        }

        return sistema;

    }

    @Transactional(readOnly = false)
    public void updatePropertyStatus(Long id, Boolean status) throws ResourceNotFoundException {
        sistemaRepository.updateStatusSistema(id, status);
    }

    @Transactional(readOnly = false)
    public void deleteById(Long id) throws ResourceNotFoundException {

        validatefindByIdExists(id);
        sistemaRepository.deleteById(id);

    }

    public List<Sistema> findAll() {
        return sistemaRepository.findAll();
    }

    public List<Sistema> findAllByUsuario(Long idUsuario) {
        return sistemaRepository.findAllByUsuario(idUsuario);
    }

    public Page<Sistema> findAll(Specification<Sistema> spec, Pageable pageable) {
        return sistemaRepository.findAll(spec, pageable);
    }

    public Long countAll(Specification<Sistema> spec) {
        return sistemaRepository.count(spec);
    }

    public Page<Sistema> findByNomeContainingAndStatusEquals(String nome, Boolean status, Pageable pageable) {
        return sistemaRepository.findByNomeContainingAndStatusEquals(nome, status, pageable);
    }

    public Optional<Sistema> findById(Long id) throws ResourceNotFoundException {
        Optional<Sistema> sistemaBanco = sistemaRepository.findById(id);
        if (!sistemaBanco.isPresent()) {
            throw new ResourceNotFoundException("O código " + id + " do sistema não foi encontrado. ");
        }
        return sistemaBanco;
    }

    public Sistema findByNomeEquals(String nome) throws ResourceNotFoundException {
        return sistemaRepository.findByNomeEquals(nome);
    }

    // Metodos Privados
    private void validatefindByIdExists(Long id) throws ResourceNotFoundException {
        Optional<Sistema> sistemaBanco = this.findById(id);
        if (!sistemaBanco.isPresent()) {
            throw new ResourceNotFoundException("O código " + id + " do sistema não foi encontrado. ");
        }
    }

    public Collection<IDAndNomeGenericoProjection> findByStatusTrue() {
        return sistemaRepository.findByStatusTrue(IDAndNomeGenericoProjection.class);
    }

    public Collection<IDAndNomeGenericoProjection> findBySistemaUsuarioSelectedStatusTrue(Long usuarioID) {
        return sistemaRepository.findBySistemaUsuarioSelectedStatusTrue(usuarioID, IDAndNomeGenericoProjection.class);
    }

    public Collection<IDAndNomeGenericoProjection> findBySistemaUsuarioAvailableStatusTrue(Long usuarioID) {
        return sistemaRepository.findBySistemaUsuarioAvailableStatusTrue(usuarioID, IDAndNomeGenericoProjection.class);
    }

    public GenericoPinkListProjection findBySistemaPinkListProjection(Long usuarioID) throws ResourceNotFoundException {

        if (usuarioID != null) {
            usuarioService.validatefindByIdExists(usuarioID);
        }

        GenericoPinkListProjection permissoesPinkListProjection = new GenericoPinkListProjection();

        Collection<IDAndNomeGenericoProjection> sistemasSelecionadas = this
                .findBySistemaUsuarioSelectedStatusTrue(usuarioID);
        Collection<IDAndNomeGenericoProjection> sistemasDisponiveis = this
                .findBySistemaUsuarioAvailableStatusTrue(usuarioID);

        permissoesPinkListProjection.setRegistrosDisponiveis(sistemasDisponiveis);
        permissoesPinkListProjection.setRegistrosSelecionados(sistemasSelecionadas);

        return permissoesPinkListProjection;
    }

}
