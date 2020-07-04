package br.com.obt.sca.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import br.com.obt.sca.api.model.Perfil;
import br.com.obt.sca.api.model.PerfilPermissao;
import br.com.obt.sca.api.repository.PerfilPermissaoRepository;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ResourceParameterNullException;
import br.com.obt.sca.api.service.exception.ServiceException;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
@Service
public class PerfilPermissaoService {

    // private static final Logger logger =
    // LoggerFactory.getLogger(PerfilPermissaoService.class);
    @Autowired
    private PerfilPermissaoRepository perfilPermissaoRepository;

    @Transactional(readOnly = false)
    public void deleteByPerfilPermissao(Long idPerfil, Long idPermissao)
            throws ResourceNotFoundException, ResourceParameterNullException {
        validatePerfilPermissao(idPerfil, idPermissao);
        perfilPermissaoRepository.deleteByPerfilPermissao(idPerfil, idPermissao);
    }

    @Transactional(readOnly = false)
    public PerfilPermissao save(PerfilPermissao perfilPermissao)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        Long idPerfil = perfilPermissao.getPerfilPermissaoPK().getPerfil().getId();
        Long idPermissao = perfilPermissao.getPerfilPermissaoPK().getPermissao().getId();

        validatePerfilPermissao(idPerfil, idPermissao);
        return perfilPermissaoRepository.save(perfilPermissao);

    }

    @Transactional(readOnly = false)
    public List<PerfilPermissao> savePermissions(List<PerfilPermissao> perfilPermissoes)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        for (PerfilPermissao perfilPermissao : perfilPermissoes) {
            Long idPerfil = perfilPermissao.getPerfilPermissaoPK().getPerfil().getId();
            Long idPermissao = perfilPermissao.getPerfilPermissaoPK().getPermissao().getId();

            validatePerfilPermissao(idPerfil, idPermissao);
        }

        return perfilPermissaoRepository.saveAll(perfilPermissoes);

    }

    @Transactional(readOnly = false)
    public List<PerfilPermissao> salvarPermissoesIDs(Long idPerfil, Set<Long> ids)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        perfilPermissaoRepository.deleteByPerfilPermissoes(idPerfil);

        for (Long idPermissao : ids) {
            validatePerfilPermissao(idPerfil, idPermissao);
            perfilPermissaoRepository.savePerfilPermissao(idPerfil, idPermissao);
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = false)
    public List<PerfilPermissao> savePerfilPermissionsIDs(Perfil perfil, Set<Long> ids)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        perfilPermissaoRepository.deleteByPerfilPermissoes(perfil.getId());

        // PerfilPermissao perfilPermissao = new PerfilPermissao();
        // perfilPermissao.setDataHoraCadastro(LocalDateTime.now());
        // perfilPermissao.setDataHoraAlteracao(LocalDateTime.now());
        for (Long idPermissao : ids) {
            validatePerfilPermissao(perfil.getId(), idPermissao);
            perfilPermissaoRepository.savePerfilPermissao(perfil.getId(), idPermissao);
        }

        return new ArrayList<PerfilPermissao>();

    }

    public Optional<PerfilPermissao> findByIdPerfilIdPermissao(Long idPerfil, Long idPermissao)
            throws ResourceNotFoundException {

        Optional<PerfilPermissao> perfilPermissaoBanco = perfilPermissaoRepository.findByIdPerfilIdPermissao(idPerfil,
                idPermissao);
        if (!perfilPermissaoBanco.isPresent()) {
            throw new ResourceNotFoundException("O código " + idPerfil + " do perfil e o código " + idPermissao
                    + " da permissão não foram encontrados. ");
        }
        return perfilPermissaoBanco;
    }

    // metodos privados
    private void validatePerfilPermissao(Long idPerfil, Long idPermissao)
            throws ResourceNotFoundException, ResourceParameterNullException {

        if ((idPerfil == null) || (idPermissao == 0)) {
            throw new ResourceParameterNullException("Parametros invalidos");
        }

    }

}
