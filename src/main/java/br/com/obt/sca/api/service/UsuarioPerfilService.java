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
import br.com.obt.sca.api.model.UsuarioPerfil;
import br.com.obt.sca.api.repository.UsuarioPerfilRepository;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ResourceParameterNullException;
import br.com.obt.sca.api.service.exception.ServiceException;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
@Service
public class UsuarioPerfilService {

    @Autowired
    private UsuarioPerfilRepository usuarioPerfilRepository;

    @Transactional(readOnly = false)
    public void deleteByUsuarioPerfil(Long idUsuario, Long idPerfil)
            throws ResourceNotFoundException, ResourceParameterNullException {
        usuarioPerfilRepository.deleteByUsuarioPerfil(idUsuario, idPerfil);
    }

    @Transactional(readOnly = false)
    public List<UsuarioPerfil> savePermissions(List<UsuarioPerfil> usuariosPerfis)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        return usuarioPerfilRepository.saveAll(usuariosPerfis);

    }

    @Transactional(readOnly = false)
    public List<UsuarioPerfil> saveUsuarioPerfilIDS(Usuario usuario, Set<Long> ids)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        usuarioPerfilRepository.deleteByPerfilPermissoes(usuario.getId());

        // PerfilPermissao perfilPermissao = new PerfilPermissao();
        // perfilPermissao.setDataHoraCadastro(LocalDateTime.now());
        // perfilPermissao.setDataHoraAlteracao(LocalDateTime.now());
        for (Long idPermissao : ids) {
            usuarioPerfilRepository.saveUsuarioPerfil(usuario.getId(), idPermissao);
        }
        return new ArrayList<>();

    }

    public Optional<UsuarioPerfil> findByIdUsuarioIdPerfilId(Long idUsuario, Long idPerfil)
            throws ResourceNotFoundException {

        Optional<UsuarioPerfil> perfilPermissaoBanco = usuarioPerfilRepository.findByIdUsuarioPerfilId(idUsuario,
                idPerfil);
        if (!perfilPermissaoBanco.isPresent()) {
            throw new ResourceNotFoundException("O código " + idPerfil + " do perfil e o código " + idUsuario
                    + " da permissão não foram encontrados. ");
        }
        return perfilPermissaoBanco;
    }

}
