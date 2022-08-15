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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.obt.sca.api.config.property.OuterBoxTechSCAApiProperty;
import br.com.obt.sca.api.config.threadexecutor.ThreadEnviarEmail;
import br.com.obt.sca.api.mail.Mailer;
import br.com.obt.sca.api.model.Perfil;
import br.com.obt.sca.api.model.Permissao;
import br.com.obt.sca.api.model.Usuario;
import br.com.obt.sca.api.model.enumeration.TipoAutenticacao;
import br.com.obt.sca.api.projections.usuario.UsuarioAndPerfisAndSistemasProjection;
import br.com.obt.sca.api.projections.usuario.UsuarioAndPermissaoProjection;
import br.com.obt.sca.api.repository.UsuarioRepository;
import br.com.obt.sca.api.repository.superclass.GenericRepository;
import br.com.obt.sca.api.service.exception.ResourceAdministratorNotUpdateException;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ResourceParameterNullException;
import br.com.obt.sca.api.service.exception.ServiceException;
import br.com.obt.sca.api.service.superclass.GenericService;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
@Service
public class UsuarioService extends GenericService<Usuario> {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioPerfilService usuarioPerfilService;

    @Autowired
    private UsuarioPermissaoService usuarioPermissaoService;

    @Autowired
    private UsuarioSistemaService usuarioSistemaService;

    @Autowired
    private PermissaoService permissaoService;

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private Mailer mailer;

    @Autowired
    private OuterBoxTechSCAApiProperty property;

    @Autowired
    private TokenService tokenService;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    public UsuarioService(GenericRepository<Usuario, Long> repository) {
        super(repository);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Usuario save(Usuario usuario)
            throws ResourceAlreadyExistsException, ResourceNotFoundException {

        if (usuario == null) {
            throw new ResourceNotFoundException("O usuário não foi encontrado!");
        }

        if (usuario.getId() != null && usuario.getId() == 1l) {
            throw new ResourceNotFoundException("O usuário administrador não pode ser alterado!");
        }

        String senha = usuario.getSenha();

        boolean isMesmaSenha = false;

        // Criptografando a senha.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (usuario.getId() != null) {
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
        if (usuario.getTipoAutenticacao() == null) {
            usuario.setTipoAutenticacao(TipoAutenticacao.SCA);
        }

        //se nao e a mesma senha, salva.
        if (!isMesmaSenha) {
            usuario.setSenha(encoder.encode(senha));
        }
        usuarioSalvo = usuarioRepository.save(usuario);

        logger.info("depois de salvar usuário.");

        ThreadEnviarEmail task = new ThreadEnviarEmail(this, usuarioSalvo);
        executorService.execute(task);

        return usuarioSalvo;

    }

    @Transactional(readOnly = false)
    public UsuarioAndPerfisAndSistemasProjection salvarSistemas(UsuarioAndPerfisAndSistemasProjection usuarioAndSistemasProjection)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        usuarioSistemaService.salvarSistemasIDs(usuarioAndSistemasProjection.getId(), usuarioAndSistemasProjection.getIdsSistemas());

        return usuarioAndSistemasProjection;
    }

    // @Async("threadPoolTaskExecutor")
    public void enviarEmailUsuario(Usuario usuario) throws ResourceNotFoundException {

        System.out.println("Thread :" + Thread.currentThread().getName());
        
        if(usuario.getEmail() == null)
        	usuario = usuarioRepository.getOne(usuario.getId());

        List<String> emails = new ArrayList<String>();
        emails.add(usuario.getEmail());
        //emails.add("marcelo.nobre@outerboxtech.com.br");
        //emails.add("jose.silva@outerboxtech.com.br");
        //emails.add("vinicius.assis@outerboxtech.com.br");

        String token = tokenService.criarToken(usuario.getId());

        Map<String, Object> variaveis = new HashMap<>();
        variaveis.put("emails", emails);
        variaveis.put("url", property.getUrlApiScaClient() + "/recuperar_senha.jsf?token=" + token);

        logger.info("antes de enviar o email.");
        mailer.enviarEmailConfirmacaoDePermissaoCadastrada(emails,
                "Usuario : " + usuario.getLogin() + " cadastrado com sucesso!",
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
    @Override
    public void updatePropertyStatus(Long idUsuario, Boolean status)
            throws ResourceNotFoundException {

        if (idUsuario == null) {
            throw new ResourceNotFoundException("Usuário não encontrado!");
        }

        if (idUsuario == 1l) {
            throw new ResourceNotFoundException("O status do administrador não pode ser alterado!");
        } else {
            Optional<Usuario> usuarioBanco = findById(idUsuario);
            Usuario usuario = usuarioBanco.get();
            usuario.setStatus(status);
            usuarioRepository.save(usuario);
        }
    }

    public Page<Usuario> findByNomeContaining(String nome, Pageable pageable) {
        return usuarioRepository.findByNomeContaining(nome, pageable);
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

    public Optional<Usuario> findByEmailOrLogin(String emailOrLogin) {
        return usuarioRepository.findByEmailOrLogin(emailOrLogin);
    }

    public Optional<Usuario> findByLogin(String login) {
        return usuarioRepository.findByLogin(login);
    }

    @Transactional(readOnly = false)
    public UsuarioAndPerfisAndSistemasProjection saveUsuarioAndPerfis(UsuarioAndPerfisAndSistemasProjection usuarioAndPerfisProjection)
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
    public UsuarioAndPermissaoProjection saveUsuarioAndPermissoes(UsuarioAndPermissaoProjection usuarioAndPermissoesProjection)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException,
            ResourceAdministratorNotUpdateException {

        Usuario usuario = new Usuario();
        usuario.setId(usuarioAndPermissoesProjection.getId());

        usuarioPermissaoService.saveUsuarioPermissoesIDS(usuario, usuarioAndPermissoesProjection.getIdsPermissoes());

        Set<Long> idSistemas = new TreeSet<>();
        for (Long idPermissao : usuarioAndPermissoesProjection.getIdsPermissoes()) {
            Optional<Permissao> permissaoBanco = permissaoService.findById(idPermissao);
            idSistemas.add(permissaoBanco.get().getSistema().getId());
        }
        if (!idSistemas.isEmpty()) {
            usuarioSistemaService.saveUsuarioSistemaIDS(usuario, idSistemas);
        }
        return usuarioAndPermissoesProjection;
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

    @Override
    public Optional<Usuario> updateFields(Optional<Usuario> beanBanco, Usuario bean) {
        return beanBanco;
    }

}
