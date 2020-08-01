package br.com.obt.sca.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.obt.sca.api.model.Usuario;
import br.com.obt.sca.api.model.UsuarioPermissao;
import br.com.obt.sca.api.repository.UsuarioPermissaoRepository;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ResourceParameterNullException;
import br.com.obt.sca.api.service.exception.ServiceException;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
@Service
public class UsuarioPermissaoService {

    @Autowired
    private UsuarioPermissaoRepository usuarioPermissaoRepository;

    @Transactional(readOnly = false)
    public void deleteByUsuarioPermissao(Long idUsuario, Long idPermissao)
            throws ResourceNotFoundException, ResourceParameterNullException {
        usuarioPermissaoRepository.deleteByUsuarioPermissao(idUsuario, idPermissao);
    }

    @Transactional(readOnly = false)
    public List<UsuarioPermissao> savePermissioes(List<UsuarioPermissao> usuariosPermissoes)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {
        return usuarioPermissaoRepository.saveAll(usuariosPermissoes);
    }

    @Transactional(readOnly = false)
    public List<UsuarioPermissao> saveUsuarioPermissoesIDS(Usuario usuario, Set<Long> ids)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        usuarioPermissaoRepository.deleteByPermissaoPermissoes(usuario.getId());

        for (Long idPermissao : ids) {
            usuarioPermissaoRepository.saveUsuarioPermissao(usuario.getId(), idPermissao);
        }
        return new ArrayList<>();

    }

    public Optional<UsuarioPermissao> findByIdUsuarioIdPermissaoId(Long idUsuario, Long idPermissao)
            throws ResourceNotFoundException {

        Optional<UsuarioPermissao> permissaoPermissaoBanco = usuarioPermissaoRepository.findByIdUsuarioPermissaoId(idUsuario,
                idPermissao);
        if (!permissaoPermissaoBanco.isPresent()) {
            throw new ResourceNotFoundException("O código " + idPermissao + " do permissao e o código " + idUsuario
                    + " da permissão não foram encontrados. ");
        }
        return permissaoPermissaoBanco;
    }

}
