package br.com.obt.sca.api.resource;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import br.com.obt.sca.api.resource.filter.BaseFilter;
import br.com.obt.sca.api.resource.filter.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.util.UriComponentsBuilder;

import br.com.obt.sca.api.event.RecursoCriadoEvent;
import br.com.obt.sca.api.model.Permissao;
import br.com.obt.sca.api.projections.GenericoPinkListProjection;
import br.com.obt.sca.api.service.PermissaoService;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "permissoes", description = "Serviço de permissões")
@ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "Lista de permissões executada com sucesso")
            ,@ApiResponse(code = 201, message = "Permissão cadastrada com sucesso")
            ,@ApiResponse(code = 301, message = "O recurso que você estava tentando acessar foi encontrado")
            ,@ApiResponse(code = 401, message = "Você não está autorizado para visualizar este recurso")
            ,@ApiResponse(code = 403, message = "O recurso que você estava tentando acessar é restrito")
            ,@ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado")
        }
)
@RestController
@RequestMapping("/permissoes")
public class PermissaoResource {

    @Autowired
    private PermissaoService permissaoService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @ApiOperation(value = "Listar das permissão paginada por nome , sistema e status", response = List.class)
    @GetMapping(value = "/paginacao")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PERMISSAO')")
    public ResponseEntity<Page<Permissao>> findByNomeContainingPagination(
            @RequestParam(required = false, defaultValue = "") String nome,
            @RequestParam(required = false) Long idSistema,
            @RequestParam(required = false, defaultValue = "true") Boolean status,
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @PageableDefault(size = 10) Pageable pageable) {

        final PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("asc".equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort));

        Page<Permissao> permissoesPage;
        if ((idSistema != null) && (idSistema.longValue() > 0)) {
            permissoesPage = permissaoService.findByNomeContainingAndSistemaAndStatus(idSistema, nome, status,
                    pageRequest);
        } else {
            permissoesPage = permissaoService.findByNomeContainingAndStatusEquals(nome, status, pageable);
        }

        if (permissoesPage.getContent().isEmpty()) {
            return new ResponseEntity<Page<Permissao>>(HttpStatus.NO_CONTENT);
        } else {
            long totalPermissoes = permissoesPage.getTotalElements();
            int nbPagePermissoes = permissoesPage.getNumberOfElements();

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(permissoesPage.getTotalElements()));

            if (nbPagePermissoes < totalPermissoes) {
                headers.add("first", buildPageUri(PageRequest.of(0, permissoesPage.getSize())));
                headers.add("last",
                        buildPageUri(PageRequest.of(permissoesPage.getTotalPages() - 1, permissoesPage.getSize())));
                if (permissoesPage.hasNext()) {
                    headers.add("next", buildPageUri(permissoesPage.nextPageable()));
                }
                if (permissoesPage.hasPrevious()) {
                    headers.add("prev", buildPageUri(permissoesPage.previousPageable()));
                }
                return new ResponseEntity<>(permissoesPage, headers, HttpStatus.PARTIAL_CONTENT);
            } else {
                return new ResponseEntity<Page<Permissao>>(permissoesPage, headers, HttpStatus.OK);
            }
        }
    }

    @ApiOperation(value = "Lista paginada de permissões", response = List.class)
    @GetMapping(value = "/paginacao/{page}/{limit}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PERMISSAO')")
    public List<Permissao> findAll(
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String nomeSistema,
            @RequestParam(required = false) Boolean status,
            @PathVariable int page,
            @PathVariable int limit) {

        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by("asc".equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort));

        Specification spec = Specification.where(new BaseFilter("nome", SearchCriteria.CONTAINS, nome))
                .and(new BaseFilter("nome", SearchCriteria.CONTAINS, nomeSistema, "sistema"))
                .and(new BaseFilter("descricao", SearchCriteria.CONTAINS, descricao))
                .and(new BaseFilter("status", SearchCriteria.EQUALS, status));

        return permissaoService.findAll(spec, pageRequest).getContent();
    }

    @ApiOperation(value = "Retorna a quantidade de permissões de acordo com os filtros", response = List.class)
    @GetMapping(value = "/count/all/")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PERMISSAO')")
    public Long countAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String nomeSistema,
            @RequestParam(required = false) Boolean status) {

        Specification spec = Specification.where(new BaseFilter("nome", SearchCriteria.CONTAINS, nome))
                .and(new BaseFilter("nome", SearchCriteria.CONTAINS, nomeSistema, "sistema"))
                .and(new BaseFilter("descricao", SearchCriteria.CONTAINS, descricao))
                .and(new BaseFilter("status", SearchCriteria.EQUALS, status));

        Long retorno = permissaoService.countAll(spec);

        return retorno;
    }

    @ApiOperation(value = "Salvar uma permissão", response = List.class)
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = {
        MediaType.APPLICATION_JSON_UTF8_VALUE})
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PERMISSAO') and #oauth2.hasScope('write')")
    public ResponseEntity<Permissao> save(@Valid @RequestBody Permissao permissao, HttpServletResponse response,
            UriComponentsBuilder ucBuilder) throws ResourceAlreadyExistsException, ResourceNotFoundException {

        Permissao permissaoSalva = permissaoService.save(permissao);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, permissaoSalva.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(permissaoSalva);
    }

    @ApiOperation(value = "Pesquisa por ID", response = List.class)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PERMISSAO') and #oauth2.hasScope('read')")
    public ResponseEntity<Permissao> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Permissao> permissao = permissaoService.findById(id);
        return permissao.isPresent() ? ResponseEntity.ok(permissao.get()) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Atualizar o status")
    @PutMapping(value = "/ativo/{id}")
    @PreAuthorize("hasAuthority('ROLE_STATUS_PERMISSAO') and #oauth2.hasScope('write')")
    public void updatePropertyStatus(@PathVariable Long id, @RequestBody Boolean status)
            throws ResourceNotFoundException {
        permissaoService.updatePropertyStatus(id, status);
    }

    @ApiOperation(value = "Excluir permissão - Default : Só o administrador poderá fazer essa exclusão fisica.")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_REMOVER_PERMISSAO') and #oauth2.hasScope('write')")
    public void deleteById(@PathVariable Long id) throws ResourceNotFoundException {
        permissaoService.deleteById(id);
    }

    @ApiOperation(value = "Duas listas : Vinculadas e Não vinculadas ao perfil ", response = List.class)
    @GetMapping(value = "/ativos/picklist")
    public GenericoPinkListProjection findByPermissaoPinkListProjection(@RequestParam(required = false) Long perfilid) {
        return permissaoService.findByPermissaoPinkListProjection(perfilid);
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

    @ApiOperation(value = " Permissões por nome da permissão e usuario", response = List.class)
    @GetMapping(value = "/ativos/sistema/{permissao}/{idSistema}")
    public Permissao findPermissaoPorSistema(@PathVariable String permissao, @PathVariable Long idSistema) throws ResourceNotFoundException {
        return permissaoService.findPermissaoPorSistema(permissao, idSistema);
    }

    // Metodos Privados
    private String buildPageUri(Pageable page) {
        return fromUriString("/permissoes").query("page={page}&size={size}")
                .buildAndExpand(page.getPageNumber(), page.getPageSize()).toUriString();
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
