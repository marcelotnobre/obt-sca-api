package br.com.obt.sca.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.obt.sca.api.config.property.OuterBoxTechSCAApiProperty;
import br.com.obt.sca.api.config.threadexecutor.ThreadEnviarEmail;
import br.com.obt.sca.api.mail.Mailer;
import br.com.obt.sca.api.model.Perfil;
import br.com.obt.sca.api.model.Usuario;
import br.com.obt.sca.api.model.enumeration.TipoAutenticacao;
import br.com.obt.sca.api.projections.usuario.UsuarioAndPerfisAndSistemasProjection;
import br.com.obt.sca.api.projections.usuario.UsuarioAndPerfisProjection;
import br.com.obt.sca.api.projections.usuario.UsuarioAndSistemasProjection;
import br.com.obt.sca.api.repository.UsuarioRepository;
import br.com.obt.sca.api.service.exception.ResourceAdministratorNotUpdateException;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ResourceParameterNullException;
import br.com.obt.sca.api.service.exception.ServiceException;

//@formatter:off
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
//@formatter:on
@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioPerfilService usuarioPerfilService;

    @Autowired
    private UsuarioSistemaService usuarioSistemaService;

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private Mailer mailer;

    @Autowired
    private OuterBoxTechSCAApiProperty property;

    @Autowired
    private TokenService tokenService;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Usuario save(Usuario usuario)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceAdministratorNotUpdateException {

        if ((usuario != null) && (usuario.getId() != null) && (usuario.getId() == 1l)) {
            throw new ResourceAdministratorNotUpdateException("O usuário administrador não pode ser alterado!");
        }
        
        String senha = usuario.getSenha();
        
        boolean isMesmaSenha = false;

        // Criptografando a senha.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if ((usuario != null) && (usuario.getId() != null)) {
            Optional<Usuario> usuarioBanco = this.findById(usuario.getId());
            
            isMesmaSenha = usuarioBanco.get().getSenha().equals(usuario.getSenha());

            // Setando os campos que são alterados na tela.
            usuarioBanco.get().setLogin(usuario.getLogin());
            usuarioBanco.get().setEmail(usuario.getEmail());
            usuarioBanco.get().setStatus(usuario.getStatus());
            usuarioBanco.get().setTipoAutenticacao(usuario.getTipoAutenticacao());

            if (StringUtils.isBlank(usuario.getTipoAutenticacao().getLabel())) {
                usuarioBanco.get().setTipoAutenticacao(TipoAutenticacao.SCA);
            }

            BeanUtils.copyProperties(usuarioBanco.get(), usuario, "id");
        }

        logger.info("antes de salvar usuário.");
        Usuario usuarioSalvo = usuario;
        if (usuario != null) {
            if (usuario.getTipoAutenticacao() == null) {
                usuario.setTipoAutenticacao(TipoAutenticacao.SCA);
            }
            
            //se nao e a mesma senha, salva.
            if(!isMesmaSenha) {
                usuario.setSenha(encoder.encode(senha));
            }
            usuarioSalvo = usuarioRepository.save(usuario);
        }

        logger.info("depois de salvar usuário.");

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        ThreadEnviarEmail task = new ThreadEnviarEmail(this, usuarioSalvo);
        executorService.execute(task);

        return usuarioSalvo;

    }

    @Transactional(readOnly = false)
    public UsuarioAndSistemasProjection salvarSistemas(UsuarioAndSistemasProjection usuarioAndSistemasProjection)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        usuarioSistemaService.salvarSistemasIDs(usuarioAndSistemasProjection.getId(), usuarioAndSistemasProjection.getIdsSistemas());

        return usuarioAndSistemasProjection;
    }

    // @Async("threadPoolTaskExecutor")
    public void enviarEmailUsuario(Usuario usuario) throws ResourceNotFoundException {

        System.out.println("Thread :" + Thread.currentThread().getName());

        Optional<Usuario> usuarioBanco = findById(usuario.getId());

        List<String> emails = new ArrayList<String>();
        emails.add(usuarioBanco.get().getEmail());
        //emails.add("marcelo.nobre@outerboxtech.com.br");
        //emails.add("jose.silva@outerboxtech.com.br");
        //emails.add("vinicius.assis@outerboxtech.com.br");

        String token = tokenService.criarToken(usuarioBanco.get().getId());

        Map<String, Object> variaveis = new HashMap<>();
        variaveis.put("emails", emails);
        variaveis.put("url", property.getUrlApiScaClient() + "/recuperar_senha.jsf?token=" + token);

        logger.info("antes de enviar o email.");
        mailer.enviarEmailConfirmacaoDePermissaoCadastrada(emails,
                "Usuario : " + usuarioBanco.get().getLogin() + " cadastrado com sucesso!",
                "mail/email-recuperacao-senha", variaveis);
        logger.info("Envio de e-mail de aviso concluído.");

    }

    @Transactional(readOnly = false)
    public void alterarSenha(String senha, String token)
            throws ResourceNotFoundException {
        DecodedJWT decoded = tokenService.verify(token);

        boolean expirou = tokenService.isExpirou(decoded.getExpiresAt());

        if (!expirou) {
            Long idUsuario = Long.parseLong(decoded.getIssuer());

            Optional<Usuario> usuarioBanco = findById(idUsuario);
            Usuario usuario = usuarioBanco.get();
            usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
            usuarioRepository.save(usuario);
        }
    }

    @Transactional(readOnly = false)
    public void updatePropertyStatus(Long idUsuario, Boolean status)
            throws ResourceNotFoundException, ResourceAdministratorNotUpdateException {

        if ((idUsuario != null) && (idUsuario == 1l)) {
            throw new ResourceAdministratorNotUpdateException("O status do administrador não pode ser alterado!");
        } else {
            if (idUsuario != null) {
                Optional<Usuario> usuarioBanco = findById(idUsuario);
                Usuario usuario = usuarioBanco.get();
                usuario.setStatus(status);
                usuarioRepository.save(usuario);
            }
        }

    }

    @Transactional(readOnly = false)
    public void deleteById(Long id) throws ResourceNotFoundException {

        validatefindByIdExists(id);
        usuarioRepository.deleteById(id);

    }

    public Page<Usuario> findByNomeContaining(String nome, Pageable pageable) {
        return usuarioRepository.findByNomeContaining(nome, pageable);
    }

    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Page<Usuario> findAll(Specification<Usuario> spec, Pageable pageable) {
        return usuarioRepository.findAll(spec, pageable);
    }

    public Long countAll(Specification<Usuario> spec) {
        return usuarioRepository.count(spec);
    }

    public Page<Usuario> findByEmailAndLogin(String login, String email, Pageable pageable) {
        return usuarioRepository.findByEmailAndLogin(login, email, pageable);
    }

    public Page<Usuario> findByEmailOrLogin(String emailOrLogin, Pageable pageable) {
        return usuarioRepository.findByEmailOrLogin(emailOrLogin, pageable);
    }

    public Long countUsuario() {
        return usuarioRepository.count();
    }

    public Optional<Usuario> findById(Long id) throws ResourceNotFoundException {
        Optional<Usuario> usuarioBanco = usuarioRepository.findById(id);
        if (!usuarioBanco.isPresent()) {
            throw new ResourceNotFoundException("O código " + id + " do usuário não foi encontrado. ");
        }
        return usuarioBanco;
    }

    public Optional<Usuario> findByEmailOrLogin(String emailOrLogin) {
        return usuarioRepository.findByEmailOrLogin(emailOrLogin);
    }
    
    public Optional<Usuario> findByLogin(String login) {
	return usuarioRepository.findByLogin(login);
    }

    @Transactional(readOnly = false)
    public UsuarioAndPerfisProjection saveUsuarioAndPerfis(UsuarioAndPerfisProjection usuarioAndPerfisProjection)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException,
            ResourceAdministratorNotUpdateException {

        Usuario usuario = new Usuario();
        usuario.setId(usuarioAndPerfisProjection.getId());
        //BeanUtils.copyProperties(usuarioAndPerfisProjection, usuario, "idsPerfis");
        //Usuario usuarioSalvo = save(usuario);

        usuarioPerfilService.saveUsuarioPerfilIDS(usuario, usuarioAndPerfisProjection.getIdsPerfis());

        Set<Long> idSistemas = new TreeSet<>();
        for (Long idPerfil : usuarioAndPerfisProjection.getIdsPerfis()) {

            Optional<Perfil> perfilBanco = perfilService.findById(idPerfil);
            idSistemas.add(perfilBanco.get().getSistema().getId());

        }
        if (!idSistemas.isEmpty()) {
            usuarioSistemaService.saveUsuarioSistemaIDS(usuario, idSistemas);
        }

        return usuarioAndPerfisProjection;

    }

    @Transactional(readOnly = false)
    public UsuarioAndPerfisAndSistemasProjection saveUsuarioAndPerfisAndSistemas(
            UsuarioAndPerfisAndSistemasProjection usuarioAndPerfisAndSistemasProjection)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException,
            ResourceAdministratorNotUpdateException {

        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioAndPerfisAndSistemasProjection, usuario, "idsPerfis", "idsSistemas");
        Usuario usuarioSalvo = save(usuario);

        usuarioPerfilService.saveUsuarioPerfilIDS(usuarioSalvo, usuarioAndPerfisAndSistemasProjection.getIdsPerfis());

        usuarioSistemaService.saveUsuarioSistemaIDS(usuarioSalvo,
                usuarioAndPerfisAndSistemasProjection.getIdsSistemas());

        return usuarioAndPerfisAndSistemasProjection;

    }

    // Metodos Privados
    public void validatefindByIdExists(Long id) throws ResourceNotFoundException {
        Optional<Usuario> usuarioBanco = this.findById(id);
        if (!usuarioBanco.isPresent()) {
            throw new ResourceNotFoundException("O código " + id + " do usuário não foi encontrado. ");
        }
    }

}
