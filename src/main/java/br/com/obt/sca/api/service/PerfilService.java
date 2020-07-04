package br.com.obt.sca.api.service;

import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import br.com.obt.sca.api.model.Perfil;
import br.com.obt.sca.api.projections.GenericoPickListProjection;
import br.com.obt.sca.api.projections.IDAndNomeGenericoProjection;
import br.com.obt.sca.api.projections.perfil.PerfilAndPermissoesProjection;
import br.com.obt.sca.api.repository.PerfilRepository;
import br.com.obt.sca.api.repository.superclass.GenericRepository;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ResourceParameterNullException;
import br.com.obt.sca.api.service.exception.ServiceException;
import br.com.obt.sca.api.service.superclass.GenericService;
import java.util.List;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
@Service
public class PerfilService extends GenericService<Perfil> {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PerfilPermissaoService perfilPermissaoService;

    @Autowired
    private SistemaService sistemaService;

    @Autowired
    public PerfilService(GenericRepository<Perfil, Long> repository) {
        super(repository);
    }

    @Override
    public Optional<Perfil> updateFields(Optional<Perfil> perfilBanco, Perfil perfil) {
        // Setando os campos que são alterados na tela.
        perfilBanco.get().setNome(perfil.getNome());
        perfilBanco.get().setDescricao(perfil.getDescricao());
        perfilBanco.get().setSistema(perfil.getSistema());
        perfilBanco.get().setDataHoraFinalVigencia(perfil.getDataHoraFinalVigencia());
        perfilBanco.get().setStatus(perfil.getStatus());
        return perfilBanco;
    }

    @Transactional(readOnly = false)
    public PerfilAndPermissoesProjection salvarPermissoes(PerfilAndPermissoesProjection perfilAndPermissoesProjection)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        perfilPermissaoService.salvarPermissoesIDs(perfilAndPermissoesProjection.getId(),
                perfilAndPermissoesProjection.getIdsPermissoes());

        return perfilAndPermissoesProjection;
    }

    public Page<Perfil> findByNomeContaining(String nome, Pageable pageable) {
        return perfilRepository.findByNomeContaining(nome, pageable);
    }

    public List<Perfil> findByPerfisDoUsuario(Long idUsuario) throws ResourceNotFoundException {
        List<Perfil> perfisBanco = perfilRepository.findByPerfisDoUsuario(idUsuario);
        if (perfisBanco.isEmpty()) {
            throw new ResourceNotFoundException("O código " + idUsuario + " do usuário não foi encontrado. ");
        }
        return perfisBanco;
    }

    public Collection<IDAndNomeGenericoProjection> findByUsuarioPerfilAvailableStatusTrue(Long usuarioid) {
        return perfilRepository.findByUsuarioPerfilSistemaAvailableStatusTrue(usuarioid,
                sistemaService.findAllIdsByUsuario(usuarioid), IDAndNomeGenericoProjection.class);
    }

    public Collection<IDAndNomeGenericoProjection> findByUsuarioPerfilSelectedStatusTrue(Long usuarioid) {
        return perfilRepository.findByUsuarioPerfilSistemaSelectedStatusTrue(usuarioid,
                sistemaService.findAllIdsByUsuario(usuarioid), IDAndNomeGenericoProjection.class);
    }

    // FOR PICKLIST
    public Collection<IDAndNomeGenericoProjection> findByUsuarioPerfilAvailableStatusTrueForPicklist(Long usuarioid) {
        return perfilRepository.findByUsuarioPerfilSistemaAvailableStatusTrueForPicklist(usuarioid,
                sistemaService.findAllIdsByUsuario(usuarioid), IDAndNomeGenericoProjection.class);
    }

    // FOR PICKLIST
    public Collection<IDAndNomeGenericoProjection> findByUsuarioPerfilSelectedStatusTrueForPicklist(Long usuarioid) {
        return perfilRepository.findByUsuarioPerfilSistemaSelectedStatusTrueForPicklist(usuarioid,
                sistemaService.findAllIdsByUsuario(usuarioid), IDAndNomeGenericoProjection.class);
    }

    public GenericoPickListProjection findByPerfilPickListProjection(Long idUsuario) {

        GenericoPickListProjection perfisPickListProjection = new GenericoPickListProjection();

        Collection<IDAndNomeGenericoProjection> perfisSelecionadas = this
                .findByUsuarioPerfilSelectedStatusTrueForPicklist(idUsuario);
        Collection<IDAndNomeGenericoProjection> perfisDisponiveis = this
                .findByUsuarioPerfilAvailableStatusTrueForPicklist(idUsuario);

        perfisPickListProjection.setRegistrosDisponiveis(perfisDisponiveis);
        perfisPickListProjection.setRegistrosSelecionados(perfisSelecionadas);

        return perfisPickListProjection;
    }

}
