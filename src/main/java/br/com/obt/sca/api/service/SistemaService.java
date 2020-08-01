package br.com.obt.sca.api.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import br.com.obt.sca.api.model.Sistema;
import br.com.obt.sca.api.projections.GenericoPickListProjection;
import br.com.obt.sca.api.projections.IDAndNomeGenericoProjection;
import br.com.obt.sca.api.repository.SistemaRepository;
import br.com.obt.sca.api.repository.superclass.GenericRepository;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ServiceException;
import br.com.obt.sca.api.service.superclass.GenericService;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
@Service
public class SistemaService extends GenericService<Sistema> {

    @Autowired
    private SistemaRepository sistemaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    public SistemaService(GenericRepository<Sistema, Long> repository) {
        super(repository);
    }

    @Override
    public Optional<Sistema> updateFields(Optional<Sistema> sistemaBanco, Sistema sistema) {
        sistemaBanco.get().setNome(sistema.getNome());
        sistemaBanco.get().setDescricao(sistema.getDescricao());
        sistemaBanco.get().setStatus(sistema.getStatus());
        sistemaBanco.get().setUrlAPI(sistema.getUrlAPI());
        sistemaBanco.get().setUrlWEB(sistema.getUrlWEB());
        return sistemaBanco;
    }

    public List<Sistema> findAllByUsuario(Long idUsuario) {
        return sistemaRepository.findAllByUsuario(idUsuario);
    }

    public List<Long> findAllIdsByUsuario(Long idUsuario) {
        List<Sistema> sistemas = sistemaRepository.findAllByUsuario(idUsuario);

        List<Long> ids = sistemas.stream()
                .map(Sistema::getId)
                .collect(Collectors.toList());
        return ids;
    }

    public Page<Sistema> findByNomeContainingAndStatusEquals(String nome, Boolean status, Pageable pageable) {
        return sistemaRepository.findByNomeContainingAndStatusEquals(nome, status, pageable);
    }

    public Sistema findByNomeEquals(String nome) throws ResourceNotFoundException {
        return sistemaRepository.findByNomeEquals(nome);
    }

    public Collection<IDAndNomeGenericoProjection> findBySistemaUsuarioSelectedStatusTrue(Long usuarioID) {
        return sistemaRepository.findBySistemaUsuarioSelectedStatusTrue(usuarioID, IDAndNomeGenericoProjection.class);
    }

    public Collection<IDAndNomeGenericoProjection> findBySistemaUsuarioAvailableStatusTrue(Long usuarioID) {
        return sistemaRepository.findBySistemaUsuarioAvailableStatusTrue(usuarioID, IDAndNomeGenericoProjection.class);
    }

    public GenericoPickListProjection findBySistemaPickListProjection(Long usuarioID) throws ResourceNotFoundException {
        if (usuarioID != null) {
            usuarioService.validateFindByIdExists(usuarioID);
        }
        GenericoPickListProjection permissoesPickListProjection = new GenericoPickListProjection();

        Collection<IDAndNomeGenericoProjection> sistemasSelecionadas = this
                .findBySistemaUsuarioSelectedStatusTrue(usuarioID);
        Collection<IDAndNomeGenericoProjection> sistemasDisponiveis = this
                .findBySistemaUsuarioAvailableStatusTrue(usuarioID);

        permissoesPickListProjection.setRegistrosDisponiveis(sistemasDisponiveis);
        permissoesPickListProjection.setRegistrosSelecionados(sistemasSelecionadas);

        return permissoesPickListProjection;
    }

}
