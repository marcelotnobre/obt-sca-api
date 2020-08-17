package br.com.obt.sca.api.resource;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import br.com.obt.sca.api.resource.filter.BaseFilter;
import br.com.obt.sca.api.resource.filter.SearchCriteria;
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
import br.com.obt.sca.api.model.Permissao;
import br.com.obt.sca.api.projections.GenericoPickListProjection;
import br.com.obt.sca.api.projections.IDAndNomeGenericoProjection;
import br.com.obt.sca.api.service.PermissaoService;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Collection;
import java.util.Map;

@Api(value = "permissoes", description = "Serviço de permissões")
@ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "Lista de permissões executada com sucesso"),
            @ApiResponse(code = 201, message = "Permissão cadastrada com sucesso"),
            @ApiResponse(code = 301, message = "O recurso que você estava tentando acessar foi encontrado"),
            @ApiResponse(code = 401, message = "Você não está autorizado para visualizar este recurso"),
            @ApiResponse(code = 403, message = "O recurso que você estava tentando acessar é restrito"),
            @ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado")
        }
)
@RestController
@RequestMapping("/permissoes")
public class PermissaoResource extends BaseResource<Permissao> {

    @Autowired
    private PermissaoService permissaoService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    protected Specification getSpecificationPaginacao(Map<String, String> map) {
        return Specification.where(new BaseFilter("nome", SearchCriteria.CONTAINS, map))
                .and(new BaseFilter("id", SearchCriteria.EQUALS, getValueMap(map, "sistema.id"), "sistema"))
                .and(new BaseFilter("descricao", SearchCriteria.CONTAINS, map))
                .and(new BaseFilter("status", SearchCriteria.EQUALS, map));
    }

    @ApiOperation(value = "Lista paginada de permissões", response = List.class)
    @GetMapping(value = "/paginacao/{page}/{limit}")
    @PreAuthorize("hasAuthority('ROLE_CRUD_PERMISSAO')")
    @Override
    public List<Permissao> findAllPaginacao(
            @RequestParam(required = false) Map<String, String> map,
            @PathVariable int page,
            @PathVariable int limit) {

        PageRequest pageRequest = getPageRequestDefault(page, limit, map);
        Specification spec = getSpecificationPaginacao(map);

        return permissaoService.findAll(spec, pageRequest).getContent();
    }

    @ApiOperation(value = "Retorna a quantidade de permissões de acordo com os filtros", response = List.class)
    @GetMapping(value = "/count/all/")
    @PreAuthorize("hasAuthority('ROLE_CRUD_PERMISSAO')")
    @Override
    public Long countAllPaginacao(@RequestParam(required = false) Map<String, String> map) {
        return permissaoService.countAll(getSpecificationPaginacao(map));
    }

    @ApiOperation(value = "Salvar uma permissao", response = List.class)
    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_CRUD_PERMISSAO') and #oauth2.hasScope('write')")
    @Override
    public ResponseEntity<Permissao> save(@Valid @RequestBody Permissao permissao, HttpServletResponse response)
            throws ResourceAlreadyExistsException, ResourceNotFoundException {
        Permissao permissaoSalva = permissaoService.save(permissao);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, permissaoSalva.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(permissaoSalva);
    }

    @ApiOperation(value = "Pesquisa por ID", response = List.class)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_CRUD_PERMISSAO') and #oauth2.hasScope('read')")
    @Override
    public ResponseEntity<Permissao> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Permissao> permissao = permissaoService.findById(id);
        return permissao.isPresent() ? ResponseEntity.ok(permissao.get()) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Lista dos ativos - dropdown ", response = List.class)
    @GetMapping(value = "/ativos/dropdown")
    @Override
    public Collection<IDAndNomeGenericoProjection> findByStatusTrue() {
        return permissaoService.findByStatusTrue();
    }

    @ApiOperation(value = "Atualizar o status")
    @PutMapping(value = "/ativo/{id}")
    @PreAuthorize("hasAuthority('ROLE_DESATIVAR_PERMISSAO') and #oauth2.hasScope('write')")
    public void updatePropertyStatus(@PathVariable Long id, @RequestBody Boolean status)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {
        permissaoService.updatePropertyStatus(id, status);
    }

    @ApiOperation(value = "Excluir permissão - Default : Só o administrador poderá fazer essa exclusão fisica.")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_CRUD_PERMISSAO') and #oauth2.hasScope('write')")
    public void deleteById(@PathVariable Long id) throws ResourceNotFoundException {
        permissaoService.deleteById(id);
    }

    @ApiOperation(value = "Duas listas : Vinculadas e Não vinculadas ao perfil ", response = List.class)
    @GetMapping(value = "/picklist/usuario_perfil")
    public GenericoPickListProjection findByPerfilPickListProjection(@RequestParam(required = false) Long perfilid) {
        return permissaoService.findByPermissaoPickListProjection(perfilid);
    }

    @ApiOperation(value = "Duas listas : Vinculadas e Não vinculadas ao perfil ", response = List.class)
    @GetMapping(value = "/picklist/usuario_permissao")
    public GenericoPickListProjection findByPermissaoPickListProjection(@RequestParam(required = false) Long usuarioID) throws ResourceNotFoundException {
        return permissaoService.findByUsuarioPickListProjection(usuarioID);
    }

    @ApiOperation(value = " Permissões  ", response = List.class)
    @GetMapping(value = "/ativos/{usuarioId}")
    public List<Permissao> findByPermissoesDoUsuario(@PathVariable Long usuarioId) throws ResourceNotFoundException {
        return permissaoService.findByPermissoesDoUsuario(usuarioId);
    }

    @ApiOperation(value = " Permissões por nome da permissão e usuario", response = List.class)
    @GetMapping(value = "/ativos/{permissao}/{idUsuario}")
    public List<Permissao> findByPermissoesDoUsuario(@PathVariable String permissao, @PathVariable Long idUsuario) throws ResourceNotFoundException {
        return permissaoService.findPermissoes(permissao, idUsuario);
    }

    @ApiOperation(value = " Permissões  ", response = List.class)
    @GetMapping(value = "/ativos/perfil/{idPerfil}")
    public List<Permissao> findByPermissoesDoPerfil(@PathVariable Long idPerfil) throws ResourceNotFoundException {
        return permissaoService.findByPermissoesDoPerfil(idPerfil);
    }

    @ApiOperation(value = " Permissões por nome da permissão e usuario", response = List.class)
    @GetMapping(value = "/ativos/sistema/{permissao}/{idSistema}")
    public Permissao findPermissaoPorSistema(@PathVariable String permissao, @PathVariable Long idSistema) throws ResourceNotFoundException {
        return permissaoService.findPermissaoPorSistema(permissao, idSistema);
    }

    /**
     * Não apagar
     *
     *
     */
    /*
    private Specification<Permissao> whereSistema(String nomeSistema) {
        return new Specification<Permissao>() {
            @Override
            public Predicate toPredicate(Root<Permissao> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                if (nomeSistema != null) {
                    Path pathSistema = root.join("sistema");
                    Path editoraNome = pathSistema.get("nome");
                    return cb.like(editoraNome, "%" + nomeSistema + "%");
                }
                return null;
            }
        };
    }*/
}
