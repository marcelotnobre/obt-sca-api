package br.com.obt.sca.api.resource;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import br.com.obt.sca.api.model.Perfil;
import br.com.obt.sca.api.projections.perfil.PerfilAndPermissoesProjection;
import br.com.obt.sca.api.service.PerfilService;
import br.com.obt.sca.api.service.exception.ResourceParameterNullException;
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
import br.com.obt.sca.api.projections.GenericoPinkListProjection;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "perfis", description = "Serviço de perfis")
@ApiResponses(value = {
    @ApiResponse(code = 200, message = "Lista de perfis executada com sucesso")
    ,@ApiResponse(code = 201, message = "Pefil cadastrado com sucesso")
    ,@ApiResponse(code = 301, message = "O recurso que você estava tentando acessar foi encontrado")
    ,@ApiResponse(code = 401, message = "Você não está autorizado para visualizar este recurso")
    ,@ApiResponse(code = 403, message = "O recurso que você estava tentando acessar é restrito")
    ,@ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado")
})
@RestController
@RequestMapping("/perfis")
public class PerfilResource {

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @ApiOperation(value = "Listar dos perfis paginada por nome", response = List.class)
    @GetMapping(value = "/paginacao")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PERFIL')")
    public ResponseEntity<Page<Perfil>> findByNomeContainingPagination(
            @RequestParam(required = false, defaultValue = "") String nome,
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @PageableDefault(size = 10) Pageable pageable) {

        final PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("asc".equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort));

        Page<Perfil> perfisPage = perfilService.findByNomeContaining(nome, pageRequest);

        if (perfisPage.getContent().isEmpty()) {
            return new ResponseEntity<Page<Perfil>>(HttpStatus.NO_CONTENT);
        } else {
            long totalPermissoes = perfisPage.getTotalElements();
            int nbPagePermissoes = perfisPage.getNumberOfElements();

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(perfisPage.getTotalElements()));

            if (nbPagePermissoes < totalPermissoes) {
                headers.add("first", buildPageUri(PageRequest.of(0, perfisPage.getSize())));
                headers.add("last", buildPageUri(PageRequest.of(perfisPage.getTotalPages() - 1, perfisPage.getSize())));
                if (perfisPage.hasNext()) {
                    headers.add("next", buildPageUri(perfisPage.nextPageable()));
                }
                if (perfisPage.hasPrevious()) {
                    headers.add("prev", buildPageUri(perfisPage.previousPageable()));
                }
                return new ResponseEntity<>(perfisPage, headers, HttpStatus.PARTIAL_CONTENT);
            } else {
                return new ResponseEntity<Page<Perfil>>(perfisPage, headers, HttpStatus.OK);
            }
        }
    }

    @ApiOperation(value = "Lista paginada de perfis", response = List.class)
    @GetMapping(value = "/paginacao/{page}/{limit}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PERFIL')")
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

        return perfilService.findAll(spec, pageRequest).getContent();
    }

    @ApiOperation(value = "Retorna a quantidade de perfis de acordo com os filtros", response = List.class)
    @GetMapping(value = "/count/all/")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PERFIL')")
    public Long countAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) String nomeSistema,
            @RequestParam(required = false) Boolean status) {

        Specification spec = Specification.where(new BaseFilter("nome", SearchCriteria.CONTAINS, nome))
                .and(new BaseFilter("nome", SearchCriteria.CONTAINS, nomeSistema, "sistema"))
                .and(new BaseFilter("descricao", SearchCriteria.CONTAINS, descricao))
                .and(new BaseFilter("status", SearchCriteria.EQUALS, status));

        Long retorno = perfilService.countAll(spec);

        return retorno;
    }

    @ApiOperation(value = "Salvar uma perfil", response = List.class)
    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PERFIL') and #oauth2.hasScope('write')")
    public ResponseEntity<Perfil> save(@Valid @RequestBody Perfil perfil, HttpServletResponse response)
            throws ResourceAlreadyExistsException, ResourceNotFoundException {
        Perfil perfilSalvo = perfilService.save(perfil);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, perfilSalvo.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(perfilSalvo);
    }

    @ApiOperation(value = "Salvar um perfil e suas permissões", response = List.class)
    @PostMapping(value = "/perfil/permissoes")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PERFIL') and #oauth2.hasScope('write')")
    public ResponseEntity<PerfilAndPermissoesProjection> saveAndPermissions(
            @RequestBody PerfilAndPermissoesProjection perfilAndPermissoesProjection,
            HttpServletResponse response)
            throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {

        PerfilAndPermissoesProjection perfilAndPermissoesProjectionSalvo = perfilService
                .salvarPermissoes(perfilAndPermissoesProjection);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, perfilAndPermissoesProjectionSalvo.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(perfilAndPermissoesProjectionSalvo);
    }

    @ApiOperation(value = "Pesquisa por ID", response = List.class)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PERFIL') and #oauth2.hasScope('read')")
    public ResponseEntity<Perfil> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Perfil> perfil = perfilService.findById(id);
        return perfil.isPresent() ? ResponseEntity.ok(perfil.get()) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Atualizar o status")
    @PutMapping(value = "/{id}/ativo")
    @PreAuthorize("hasAuthority('ROLE_STATUS_PERFIL') and #oauth2.hasScope('write')")
    public void updatePropertyStatus(@PathVariable Long id, @RequestBody Boolean status)
            throws ResourceNotFoundException {
        perfilService.updatePropertyStatus(id, status);
    }

    @ApiOperation(value = " Permissões  ", response = List.class)
    @GetMapping(value = "/ativos/{idUsuario}")
    public List<Perfil> findByPermissoesDoUsuario(@PathVariable Long idUsuario) throws ResourceNotFoundException {
        return perfilService.findByPerfisDoUsuario(idUsuario);
    }

    @ApiOperation(value = "Excluir perfil - Default : Só o administrador poderá fazer essa exclusão fisica.")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_REMOVER_PERFIL') and #oauth2.hasScope('write')")
    public void deleteById(@PathVariable Long id) throws ResourceNotFoundException {
        perfilService.deleteById(id);
    }

    @ApiOperation(value = "Duas listas : Vinculadas e Não vinculadas ao usuário ", response = List.class)
    @GetMapping(value = "/ativos/picklist")
    public GenericoPinkListProjection findByPermissaoPinkListProjection(
            @RequestParam(required = false) Long usuarioid) {
        return perfilService.findByPerfilPinkListProjection(usuarioid);
    }

    // Metodos Privados
    private String buildPageUri(Pageable page) {
        return fromUriString("/perfis").query("page={page}&size={size}")
                .buildAndExpand(page.getPageNumber(), page.getPageSize()).toUriString();
    }
}
