package br.com.obt.sca.api.resource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.obt.sca.api.event.RecursoCriadoEvent;
import br.com.obt.sca.api.model.Usuario;
import br.com.obt.sca.api.projections.IDAndNomeGenericoProjection;
import br.com.obt.sca.api.projections.usuario.UsuarioAndPerfisAndSistemasProjection;
import br.com.obt.sca.api.projections.usuario.UsuarioAndPermissaoProjection;
import br.com.obt.sca.api.resource.filter.BaseFilter;
import br.com.obt.sca.api.resource.filter.SearchCriteria;
import br.com.obt.sca.api.service.UsuarioService;
import br.com.obt.sca.api.service.exception.ResourceAdministratorNotUpdateException;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ResourceParameterNullException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Collection;

@Api(value = "usuarios", description = "Serviço de usuarios")
@ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "Lista de usuarios executado com sucesso"),
            @ApiResponse(code = 201, message = "Usuario cadastrado com sucesso"),
            @ApiResponse(code = 301, message = "O recurso que você estava tentando acessar foi encontrado"),
            @ApiResponse(code = 401, message = "Você não está autorizado para visualizar este recurso"),
            @ApiResponse(code = 403, message = "O recurso que você estava tentando acessar é restrito"),
            @ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado")
        }
)
@RestController
@RequestMapping("/usuarios")
public class UsuarioResource extends BaseResource<Usuario> {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    protected Specification getSpecificationPaginacao(Map<String, String> map) {
        return Specification.where(new BaseFilter("login", SearchCriteria.CONTAINS, map))
                .and(new BaseFilter("email", SearchCriteria.CONTAINS, map))
                .and(new BaseFilter("status", SearchCriteria.EQUALS, map));
    }

    @ApiOperation(value = "Lista paginada de usuarios", response = List.class)
    @GetMapping(value = "/paginacao/{page}/{limit}")
    @PreAuthorize("hasAuthority('ROLE_CRUD_USUARIO')")
    @Override
    public List<Usuario> findAllPaginacao(
            @RequestParam(required = false) Map<String, String> map,
            @PathVariable int page,
            @PathVariable int limit) {

        PageRequest pageRequest = getPageRequestDefault(page, limit, map);
        Specification spec = getSpecificationPaginacao(map);

        return usuarioService.findAll(spec, pageRequest).getContent();
    }

    @ApiOperation(value = "Retorna a quantidade de usuários de acordo com os filtros", response = List.class)
    @GetMapping(value = "/count/all/")
    @PreAuthorize("hasAuthority('ROLE_CRUD_USUARIO')")
    @Override
    public Long countAllPaginacao(@RequestParam(required = false) Map<String, String> map) {
        return usuarioService.countAll(getSpecificationPaginacao(map));
    }

    @ApiOperation(value = "Salvar um Usuario, seus Perfis e seus Sistemas", response = List.class)
    @PostMapping(value = "/usuario/perfis/sistemas")
    @PreAuthorize("hasAuthority('ROLE_CRUD_USUARIO') and #oauth2.hasScope('write')")
    public ResponseEntity<UsuarioAndPerfisAndSistemasProjection> saveUsuarioAndPerfisAndSistemas(
            @Valid @RequestBody UsuarioAndPerfisAndSistemasProjection usuarioAndPerfisAndSistemasProjection,
            HttpServletResponse response) throws ResourceAlreadyExistsException, ResourceNotFoundException,
            ResourceParameterNullException, ResourceAdministratorNotUpdateException {

        UsuarioAndPerfisAndSistemasProjection usuarioAndPerfisAndSistemasProjectionSalvo = usuarioService
                .saveUsuarioAndPerfisAndSistemas(usuarioAndPerfisAndSistemasProjection);
        publisher.publishEvent(
                new RecursoCriadoEvent(this, response, usuarioAndPerfisAndSistemasProjectionSalvo.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAndPerfisAndSistemasProjectionSalvo);
    }

    @ApiOperation(value = "Recuperação de senha", response = List.class)
    @PostMapping(value = "/usuario/senha/email")
    public ResponseEntity<String> enviarEmailUsuario(@Valid @RequestBody Long idUsuario) throws ResourceAlreadyExistsException, ResourceNotFoundException,
            ResourceParameterNullException, ResourceAdministratorNotUpdateException {

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        usuarioService.enviarEmailUsuario(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body("Email enviado com sucesso!");
    }

    @ApiOperation(value = "Alterar senha", response = List.class)
    @PostMapping(value = "/usuario/senha/alterar")
    public ResponseEntity<String> alterarSenha(@Valid @RequestBody Map<String, String> mapValores)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException,
            ResourceAdministratorNotUpdateException {
        String senha = mapValores.get("senha");
        String token = mapValores.get("token");

        usuarioService.alterarSenha(senha, token);

        return ResponseEntity.status(HttpStatus.CREATED).body("Senha alterada com sucesso!");
    }

    @ApiOperation(value = "Consulta Login - Usuário  ", response = List.class)
    @GetMapping(value = "/login/{emailOrLogin}")
    public Usuario findByEmailOrLogin(@PathVariable String emailOrLogin) throws ResourceNotFoundException {
        return usuarioService.findByEmailOrLogin(emailOrLogin).get();
    }

    @ApiOperation(value = "Consulta Login - Usuário autenticação")
    @GetMapping(value = "/acesso/{login}")
    public Usuario findByLogin(@PathVariable String login) throws ResourceNotFoundException {
        return usuarioService.findByLogin(login).get();
    }

    @ApiOperation(value = "Consulta Login - Usuário  ", response = List.class)
    @GetMapping(value = "/count")
    public Long countUsuario() throws ResourceNotFoundException {
        return usuarioService.countUsuario();
    }

    @ApiOperation(value = "Salvar um Usuario e seus Perfis", response = List.class)
    @PostMapping(value = "/usuario/perfis")
    @PreAuthorize("hasAuthority('ROLE_CRUD_USUARIO') and #oauth2.hasScope('write')")
    public ResponseEntity<UsuarioAndPerfisAndSistemasProjection> saveUsuarioAndPerfis(
            @Valid @RequestBody UsuarioAndPerfisAndSistemasProjection usuarioAndPerfisProjection, HttpServletResponse response)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException,
            ResourceAdministratorNotUpdateException {

        UsuarioAndPerfisAndSistemasProjection usuarioAndPerfisProjectionSalvo = usuarioService
                .saveUsuarioAndPerfis(usuarioAndPerfisProjection);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioAndPerfisProjectionSalvo.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAndPerfisProjectionSalvo);
    }

    @ApiOperation(value = "Salvar um Usuario e seus Perfis", response = List.class)
    @PostMapping(value = "/usuario/permissoes")
    @PreAuthorize("hasAuthority('ROLE_CRUD_USUARIO') and #oauth2.hasScope('write')")
    public ResponseEntity<UsuarioAndPermissaoProjection> saveUsuarioAndPermissoes(
            @Valid @RequestBody UsuarioAndPermissaoProjection usuarioAndPermissaoProjection, HttpServletResponse response)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException,
            ResourceAdministratorNotUpdateException {

        UsuarioAndPermissaoProjection usuarioAndPermissaoProjectionSalvo = usuarioService
                .saveUsuarioAndPermissoes(usuarioAndPermissaoProjection);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioAndPermissaoProjectionSalvo.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAndPermissaoProjectionSalvo);
    }

    @ApiOperation(value = "Salvar um usuario", response = List.class)
    @PostMapping()
    // @PreAuthorize("#oauth2.hasScope('write')")
    // @PreAuthorize("hasAuthority('ROLE_CRUD_USUARIO') and
    // #oauth2.hasScope('write')")
    @Override
    public ResponseEntity<Usuario> save(@Valid @RequestBody Usuario usuario, HttpServletResponse response)
            throws ResourceAlreadyExistsException, ResourceNotFoundException {
        Usuario usuarioSalva = usuarioService.save(usuario);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioSalva.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalva);
    }

    @ApiOperation(value = "Salvar um usuario e suas permissões", response = List.class)
    @PostMapping(value = "/usuario/sistemas")
    @PreAuthorize("hasAuthority('ROLE_CRUD_USUARIO') and #oauth2.hasScope('write')")
    public ResponseEntity<UsuarioAndPerfisAndSistemasProjection> saveAndSistemas(
            @RequestBody UsuarioAndPerfisAndSistemasProjection usuarioAndSistemasProjection,
            HttpServletResponse response)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        UsuarioAndPerfisAndSistemasProjection usuarioAndSistemasProjectionSalvo = usuarioService
                .salvarSistemas(usuarioAndSistemasProjection);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioAndSistemasProjectionSalvo.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAndSistemasProjectionSalvo);
    }

    @ApiOperation(value = "Pesquisa por ID", response = List.class)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_CRUD_USUARIO') and #oauth2.hasScope('read')")
    @Override
    public ResponseEntity<Usuario> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.isPresent() ? ResponseEntity.ok(usuario.get()) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Lista dos ativos - dropdown ", response = List.class)
    @GetMapping(value = "/ativos/dropdown")
    @Override
    public Collection<IDAndNomeGenericoProjection> findByStatusTrue() {
        return usuarioService.findByStatusTrue();
    }

    @ApiOperation(value = "Atualizar o status")
    @PutMapping(value = "/ativo/{id}")
    @PreAuthorize("hasAuthority('ROLE_DESATIVAR_USUARIO') and #oauth2.hasScope('write')")
    @Override
    public void updatePropertyStatus(@PathVariable(value = "id") Long id, @RequestBody Boolean status)
            throws ResourceNotFoundException {
        usuarioService.updatePropertyStatus(id, status);
    }

    @ApiOperation(value = "Excluir permissão - Default : Só o administrador poderá fazer essa exclusão fisica.")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_CRUD_USUARIO') and #oauth2.hasScope('write')")
    @Override
    public void deleteById(@PathVariable Long id) throws ResourceNotFoundException {
        usuarioService.deleteById(id);
    }

}
