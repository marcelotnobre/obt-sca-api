package br.com.obt.sca.api.resource;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
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
import br.com.obt.sca.api.projections.usuario.UsuarioAndPerfisAndSistemasProjection;
import br.com.obt.sca.api.projections.usuario.UsuarioAndPerfisProjection;
import br.com.obt.sca.api.projections.usuario.UsuarioAndSistemasProjection;
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
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;

@Api(value = "usuarios", description = "Serviço de usuarios")
@ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "Lista de usuarios executado com sucesso")
            ,@ApiResponse(code = 201, message = "Usuario cadastrado com sucesso")
            ,@ApiResponse(code = 301, message = "O recurso que você estava tentando acessar foi encontrado")
            ,@ApiResponse(code = 401, message = "Você não está autorizado para visualizar este recurso")
            ,@ApiResponse(code = 403, message = "O recurso que você estava tentando acessar é restrito")
            ,@ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado")
        }
)
@RestController
@RequestMapping("/usuarios")
public class UsuarioResource {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @ApiOperation(value = "Listar dos usuarios paginada por nome", response = List.class)
    @GetMapping(value = "/paginacao")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_USUARIO')")
    public ResponseEntity<Page<Usuario>> findByNomeContainingPagination(
            @RequestParam(required = false, defaultValue = "") String nome,
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @PageableDefault(size = 10) Pageable pageable) {

        final PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("asc".equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort));

        Page<Usuario> usuarioPage = usuarioService.findByNomeContaining(nome, pageRequest);

        if (usuarioPage.getContent().isEmpty()) {
            return new ResponseEntity<Page<Usuario>>(HttpStatus.NO_CONTENT);
        } else {
            long totalPermissoes = usuarioPage.getTotalElements();
            int nbPagePermissoes = usuarioPage.getNumberOfElements();

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(usuarioPage.getTotalElements()));

            if (nbPagePermissoes < totalPermissoes) {
                headers.add("first", buildPageUri(PageRequest.of(0, usuarioPage.getSize())));
                headers.add("last",
                        buildPageUri(PageRequest.of(usuarioPage.getTotalPages() - 1, usuarioPage.getSize())));
                if (usuarioPage.hasNext()) {
                    headers.add("next", buildPageUri(usuarioPage.nextPageable()));
                }
                if (usuarioPage.hasPrevious()) {
                    headers.add("prev", buildPageUri(usuarioPage.previousPageable()));
                }
                return new ResponseEntity<>(usuarioPage, headers, HttpStatus.PARTIAL_CONTENT);
            } else {
                return new ResponseEntity<Page<Usuario>>(usuarioPage, headers, HttpStatus.OK);
            }
        }
    }

    @ApiOperation(value = "Lista paginada de usuarios", response = List.class)
    @GetMapping(value = "/paginacao/{page}/{limit}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_USUARIO')")
    public List<Usuario> findAll(
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean status,
            @PathVariable int page,
            @PathVariable int limit) {

        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by("asc".equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort));

        Specification spec = Specification.where(new BaseFilter("login", SearchCriteria.CONTAINS, login))
                .and(new BaseFilter("email", SearchCriteria.CONTAINS, email))
                .and(new BaseFilter("status", SearchCriteria.EQUALS, status));

        return usuarioService.findAll(spec, pageRequest).getContent();
    }

    @ApiOperation(value = "Retorna a quantidade de usuários de acordo com os filtros", response = List.class)
    @GetMapping(value = "/count/all/")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_USUARIO')")
    public Long countAll(
            @RequestParam(required = false) String login,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean status) {

        Specification spec = Specification.where(new BaseFilter("login", SearchCriteria.CONTAINS, login))
                .and(new BaseFilter("email", SearchCriteria.CONTAINS, email))
                .and(new BaseFilter("status", SearchCriteria.EQUALS, status));

        Long retorno = usuarioService.countAll(spec);

        return retorno;
    }

    @ApiOperation(value = "Salvar um Usuario, seus Perfis e seus Sistemas", response = List.class)
    @PostMapping(value = "/usuario/perfis/sistemas")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO') and #oauth2.hasScope('write')")
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
        
        return ResponseEntity.status(HttpStatus.CREATED).body("Senha alterada com sucesso!");
    }

    @ApiOperation(value = "Consulta Login - Usuário  ", response = List.class)
    @GetMapping(value = "/login/{emailOrLogin}")
    public Usuario findByEmailOrLogin(@PathVariable String emailOrLogin) throws ResourceNotFoundException {
        return usuarioService.findByEmailOrLogin(emailOrLogin).get();
    }

    @ApiOperation(value = "Consulta Login - Usuário  ", response = List.class)
    @GetMapping(value = "/count")
    public Long countUsuario() throws ResourceNotFoundException {
        return usuarioService.countUsuario();
    }

    @ApiOperation(value = "Salvar um Usuario e seus Perfis", response = List.class)
    @PostMapping(value = "/usuario/perfis")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO') and #oauth2.hasScope('write')")
    public ResponseEntity<UsuarioAndPerfisProjection> saveUsuarioAndPerfis(
            @Valid @RequestBody UsuarioAndPerfisProjection usuarioAndPerfisProjection, HttpServletResponse response)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException,
            ResourceAdministratorNotUpdateException {

        UsuarioAndPerfisProjection usuarioAndPerfisProjectionSalvo = usuarioService
                .saveUsuarioAndPerfis(usuarioAndPerfisProjection);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioAndPerfisProjectionSalvo.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAndPerfisProjectionSalvo);
    }

    @ApiOperation(value = "Salvar um usuario", response = List.class)
    @PostMapping()
    // @PreAuthorize("#oauth2.hasScope('write')")
    // @PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO') and
    // #oauth2.hasScope('write')")
    public ResponseEntity<Usuario> save(@Valid @RequestBody Usuario usuario, HttpServletResponse response)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceAdministratorNotUpdateException {
        Usuario usuarioSalva = usuarioService.save(usuario);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioSalva.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalva);
    }

    @ApiOperation(value = "Salvar um usuario e suas permissões", response = List.class)
    @PostMapping(value = "/usuario/sistemas")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO') and #oauth2.hasScope('write')")
    public ResponseEntity<UsuarioAndSistemasProjection> saveAndSistemas(
            @RequestBody UsuarioAndSistemasProjection usuarioAndSistemasProjection,
            HttpServletResponse response)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        UsuarioAndSistemasProjection usuarioAndSistemasProjectionSalvo = usuarioService
                .salvarSistemas(usuarioAndSistemasProjection);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioAndSistemasProjectionSalvo.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAndSistemasProjectionSalvo);
    }

    @ApiOperation(value = "Pesquisa por ID", response = List.class)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_USUARIO') and #oauth2.hasScope('read')")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.isPresent() ? ResponseEntity.ok(usuario.get()) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Atualizar o status")
    @PutMapping(value = "/ativo/{id}")
    @PreAuthorize("hasAuthority('ROLE_STATUS_USUARIO') and #oauth2.hasScope('write')")
    public void updatePropertyStatus(@PathVariable(value = "id") Long id, @RequestBody Boolean status)
            throws ResourceNotFoundException, ResourceAdministratorNotUpdateException {
        usuarioService.updatePropertyStatus(id, status);
    }

    @ApiOperation(value = "Excluir permissão - Default : Só o administrador poderá fazer essa exclusão fisica.")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_REMOVER_USUARIO') and #oauth2.hasScope('write')")
    public void deleteById(@PathVariable Long id) throws ResourceNotFoundException {
        usuarioService.deleteById(id);
    }

    // Metodos Privados
    private String buildPageUri(Pageable page) {
        return fromUriString("/usuarios").query("page={page}&size={size}")
                .buildAndExpand(page.getPageNumber(), page.getPageSize()).toUriString();
    }

    // private Usuario getUsuarioLogado(HttpServletRequest request) {
    // Principal principal = request.getUserPrincipal();
    // return ((UsuarioSistema)principal).getUsuario();
    // }
}
