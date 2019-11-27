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
import br.com.obt.sca.api.model.UsuarioSistema;
import br.com.obt.sca.api.repository.UsuarioSistemaRepository;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ResourceParameterNullException;
import br.com.obt.sca.api.service.exception.ServiceException;

//@formatter:off
@Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = {ServiceException.class}
)
//@formatter:on
@Service
public class UsuarioSistemaService {

    @Autowired
    private UsuarioSistemaRepository usuarioSistemaRepository;

    @Transactional(readOnly = false)
    public void deleteByUsuarioSistema(Long idUsuario, Long idSistema)
            throws ResourceNotFoundException, ResourceParameterNullException {
        usuarioSistemaRepository.deleteByUsuarioSistema(idUsuario, idSistema);
    }

    @Transactional(readOnly = false)
    public List<UsuarioSistema> savePermissions(List<UsuarioSistema> usuariosSistemas)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        return usuarioSistemaRepository.saveAll(usuariosSistemas);

    }

    @Transactional(readOnly = false)
    public List<UsuarioSistema> salvarSistemasIDs(Long idUsuario, Set<Long> ids)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        usuarioSistemaRepository.deleteByUsuarioSistemas(idUsuario);

        for (Long idSistemas : ids) {
            usuarioSistemaRepository.saveUsuarioSistema(idUsuario, idSistemas);
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = false)
    public List<UsuarioSistema> saveUsuarioSistemaIDS(Usuario usuario, Set<Long> ids)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        usuarioSistemaRepository.deleteByUsuarioSistemas(usuario.getId());

        // UsuarioSistema usuarioSistema = new UsuarioSistema();
        // usuarioSistema.setDataHoraCadastro(LocalDateTime.now());
        // usuarioSistema .setDataHoraAlteracao(LocalDateTime.now());
        for (Long idSistemas : ids) {
            usuarioSistemaRepository.saveUsuarioSistema(usuario.getId(), idSistemas);
        }

        return new ArrayList<UsuarioSistema>();

    }

    public Optional<UsuarioSistema> findByIdUsuarioIdSistemaId(Long idUsuario, Long idSistema)
            throws ResourceNotFoundException {

        Optional<UsuarioSistema> usuarioSistemaBanco = usuarioSistemaRepository.findByIdUsuarioSistemalId(idUsuario,
                idSistema);
        if (!usuarioSistemaBanco.isPresent()) {
            throw new ResourceNotFoundException("O código " + idSistema + " do sistema e o código " + idUsuario
                    + " do usuario não foram encontrados. ");
        }
        return usuarioSistemaBanco;
    }

}
