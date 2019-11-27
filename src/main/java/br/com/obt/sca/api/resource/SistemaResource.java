package br.com.obt.sca.api.resource;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import br.com.obt.sca.api.resource.filter.BaseFilter;
import br.com.obt.sca.api.resource.filter.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;
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
import br.com.obt.sca.api.model.Sistema;
import br.com.obt.sca.api.projections.GenericoPinkListProjection;
import br.com.obt.sca.api.projections.IDAndNomeGenericoProjection;
import br.com.obt.sca.api.service.SistemaService;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "sistemas", description = "Serviço de Sistemas")
@ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "Lista de sistemas executada com sucesso")
            ,@ApiResponse(code = 201, message = "Sistema cadastrado com sucesso")
            ,@ApiResponse(code = 301, message = "O recurso que você estava tentando acessar foi encontrado")
            ,@ApiResponse(code = 401, message = "Você não está autorizado para visualizar este recurso")
            ,@ApiResponse(code = 403, message = "O recurso que você estava tentando acessar é restrito")
            ,@ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado")
        }
)
@RestController
@RequestMapping("/sistemas")
public class SistemaResource {

    @Autowired
    private SistemaService sistemaService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @ApiOperation(value = "Listar de sistemas paginada por nome", response = List.class)
    @GetMapping(value = "/paginacao")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_SISTEMA')")
    public ResponseEntity<Page<Sistema>> findByNomeContainingPagination(
            @RequestParam(required = false, defaultValue = "") String nome,
            @RequestParam(required = false, defaultValue = "true") Boolean status,
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @PageableDefault(size = 10) Pageable pageable) {

        final PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("asc".equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort));

        Page<Sistema> pessoasPage = sistemaService.findByNomeContainingAndStatusEquals(nome, status, pageRequest);

        if (pessoasPage.getContent().isEmpty()) {
            return new ResponseEntity<Page<Sistema>>(HttpStatus.NO_CONTENT);
        } else {
            long totalPermissoes = pessoasPage.getTotalElements();
            int nbPagePermissoes = pessoasPage.getNumberOfElements();

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(pessoasPage.getTotalElements()));

            if (nbPagePermissoes < totalPermissoes) {
                headers.add("first", buildPageUri(PageRequest.of(0, pessoasPage.getSize())));
                headers.add("last",
                        buildPageUri(PageRequest.of(pessoasPage.getTotalPages() - 1, pessoasPage.getSize())));
                if (pessoasPage.hasNext()) {
                    headers.add("next", buildPageUri(pessoasPage.nextPageable()));
                }
                if (pessoasPage.hasPrevious()) {
                    headers.add("prev", buildPageUri(pessoasPage.previousPageable()));
                }
                return new ResponseEntity<>(pessoasPage, headers, HttpStatus.PARTIAL_CONTENT);
            } else {
                return new ResponseEntity<Page<Sistema>>(pessoasPage, headers, HttpStatus.OK);
            }
        }
    }

    @ApiOperation(value = "Lista de sistemas", response = List.class)
    @GetMapping(value = "/findAll")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_SISTEMA')")
    public List<Sistema> findAll() {
        return sistemaService.findAll();
    }

    @ApiOperation(value = "Lista de sistemas", response = List.class)
    @GetMapping(value = "/findAll/{idUsuario}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_SISTEMA')")
    public List<Sistema> findAllByUsuario(@PathVariable Long idUsuario) {
        return sistemaService.findAllByUsuario(idUsuario);
    }

    @ApiOperation(value = "Lista paginada de sistemas", response = List.class)
    @GetMapping(value = "/paginacao/{page}/{limit}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_SISTEMA')")
    public List<Sistema> findAll(
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Boolean status,
            @PathVariable int page,
            @PathVariable int limit) {

        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by("asc".equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort));

        Specification spec = Specification.where(new BaseFilter("nome", SearchCriteria.CONTAINS, nome))
                .and(new BaseFilter("descricao", SearchCriteria.CONTAINS, descricao))
                .and(new BaseFilter("status", SearchCriteria.EQUALS, status));

        return sistemaService.findAll(spec, pageRequest).getContent();
    }

    @ApiOperation(value = "Retorna a quantidade de sistemas de acordo com os filtros", response = List.class)
    @GetMapping(value = "/count/all/")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_SISTEMA')")
    public Long countAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Boolean status) {

        Specification spec = Specification.where(new BaseFilter("nome", SearchCriteria.CONTAINS, nome))
                .and(new BaseFilter("descricao", SearchCriteria.CONTAINS, descricao))
                .and(new BaseFilter("status", SearchCriteria.EQUALS, status));

        Long retorno = sistemaService.countAll(spec);

        return retorno;
    }

    @ApiOperation(value = "Salvar um sistema", response = List.class)
    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_SISTEMA') and #oauth2.hasScope('write')")
    public ResponseEntity<Sistema> save(@Valid @RequestBody Sistema sistema, HttpServletResponse response)
            throws ResourceAlreadyExistsException, ResourceNotFoundException {
        Sistema sistemaSalva = sistemaService.save(sistema);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, sistemaSalva.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(sistemaSalva);
    }

    @ApiOperation(value = "Pesquisa por ID", response = List.class)
    @GetMapping(value = "/nome/{nome}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_SISTEMA') and #oauth2.hasScope('read')")
    public ResponseEntity<Sistema> getSistemasPorNome(@PathVariable String nome)
            throws ResourceAlreadyExistsException, ResourceNotFoundException {
        Sistema sistemaSalva = sistemaService.findByNomeEquals(nome);
        return ResponseEntity.status(HttpStatus.CREATED).body(sistemaSalva);
    }

    @ApiOperation(value = "Pesquisa por ID", response = List.class)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_SISTEMA') and #oauth2.hasScope('read')")
    public ResponseEntity<Sistema> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Sistema> sistema = sistemaService.findById(id);
        return sistema.isPresent() ? ResponseEntity.ok(sistema.get()) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Lista dos sistemas ativos - dropdown ", response = List.class)
    @GetMapping(value = "/ativos/dropdown")
    public Collection<IDAndNomeGenericoProjection> findByStatusTrue() {
        return sistemaService.findByStatusTrue();
    }

    @ApiOperation(value = "Atualizar o status")
    @PutMapping(value = "/ativo/{id}")
    @PreAuthorize("hasAuthority('ROLE_STATUS_SISTEMA') and #oauth2.hasScope('write')")
    public void updatePropertyStatus(@PathVariable(value = "id") Long id, @RequestBody Boolean status)
            throws ResourceNotFoundException {
        sistemaService.updatePropertyStatus(id, status);
    }

    @ApiOperation(value = "Excluir sistema - Default : Só o administrador poderá fazer essa exclusão fisica.")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_REMOVER_SISTEMA') and #oauth2.hasScope('write')")
    public void deleteById(@PathVariable Long id) throws ResourceNotFoundException {
        sistemaService.deleteById(id);
    }

    @ApiOperation(value = "Duas listas : Vinculadas e Não vinculadas ao usuário ", response = List.class)
    @GetMapping(value = "/ativos/picklist")
    public GenericoPinkListProjection findBySistemaPinkListProjection(@RequestParam(required = false) Long usuarioID)
            throws ResourceNotFoundException {
        return sistemaService.findBySistemaPinkListProjection(usuarioID);
    }

    // Metodos Privados
    private String buildPageUri(Pageable page) {
        return fromUriString("/sistemas").query("page={page}&size={size}")
                .buildAndExpand(page.getPageNumber(), page.getPageSize()).toUriString();
    }
}
