package br.com.obt.sca.api.resource;

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
import org.springframework.data.domain.PageRequest;
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
import br.com.obt.sca.api.projections.GenericoPickListProjection;
import br.com.obt.sca.api.projections.IDAndNomeGenericoProjection;
import br.com.obt.sca.api.service.SistemaService;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Map;

@Api(value = "sistemas", description = "Serviço de Sistemas")
@ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "Lista de sistemas executada com sucesso"),
            @ApiResponse(code = 201, message = "Sistema cadastrado com sucesso"),
            @ApiResponse(code = 301, message = "O recurso que você estava tentando acessar foi encontrado"),
            @ApiResponse(code = 401, message = "Você não está autorizado para visualizar este recurso"),
            @ApiResponse(code = 403, message = "O recurso que você estava tentando acessar é restrito"),
            @ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado")
        }
)
@RestController
@RequestMapping("/sistemas")
public class SistemaResource extends BaseResource<Sistema> {

    @Autowired
    private SistemaService sistemaService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    protected Specification getSpecificationPaginacao(Map<String, String> map) {
        return Specification.where(new BaseFilter("nome", SearchCriteria.CONTAINS, map))
                .and(new BaseFilter("descricao", SearchCriteria.CONTAINS, map))
                .and(new BaseFilter("status", SearchCriteria.EQUALS, map));
    }

    @ApiOperation(value = "Lista paginada de sistemas", response = List.class)
    @GetMapping(value = "/paginacao/{page}/{limit}")
    @PreAuthorize("hasAuthority('ROLE_CRUD_SISTEMA')")
    @Override
    public List<Sistema> findAllPaginacao(
            @RequestParam(required = false) Map<String, String> map,
            @PathVariable int page,
            @PathVariable int limit) {

        PageRequest pageRequest = getPageRequestDefault(page, limit, map);
        Specification spec = getSpecificationPaginacao(map);

        return sistemaService.findAll(spec, pageRequest).getContent();
    }

    @ApiOperation(value = "Retorna a quantidade de sistemas de acordo com os filtros", response = List.class)
    @GetMapping(value = "/count/all/")
    @PreAuthorize("hasAuthority('ROLE_CRUD_SISTEMA')")
    @Override
    public Long countAllPaginacao(@RequestParam(required = false) Map<String, String> map) {
        return sistemaService.countAll(getSpecificationPaginacao(map));
    }

    @ApiOperation(value = "Lista de sistemas", response = List.class)
    @GetMapping(value = "/findAll")
    @PreAuthorize("hasAuthority('ROLE_CRUD_SISTEMA')")
    public List<Sistema> findAll() {
        return sistemaService.findAll();
    }

    @ApiOperation(value = "Lista de sistemas", response = List.class)
    @GetMapping(value = "/findAll/{idUsuario}")
    @PreAuthorize("hasAuthority('ROLE_CRUD_SISTEMA')")
    public List<Sistema> findAllByUsuario(@PathVariable Long idUsuario) {
        return sistemaService.findAllByUsuario(idUsuario);
    }

    @ApiOperation(value = "Salvar um sistema", response = List.class)
    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_CRUD_SISTEMA') and #oauth2.hasScope('write')")
    @Override
    public ResponseEntity<Sistema> save(@Valid @RequestBody Sistema sistema, HttpServletResponse response)
            throws ResourceAlreadyExistsException, ResourceNotFoundException {
        Sistema sistemaSalva = sistemaService.save(sistema);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, sistemaSalva.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(sistemaSalva);
    }

    @ApiOperation(value = "Pesquisa por ID", response = List.class)
    @GetMapping(value = "/nome/{nome}")
    @PreAuthorize("hasAuthority('ROLE_CRUD_SISTEMA') and #oauth2.hasScope('read')")
    public ResponseEntity<Sistema> getSistemasPorNome(@PathVariable String nome)
            throws ResourceAlreadyExistsException, ResourceNotFoundException {
        Sistema sistemaSalva = sistemaService.findByNomeEquals(nome);
        return ResponseEntity.status(HttpStatus.CREATED).body(sistemaSalva);
    }

    @ApiOperation(value = "Pesquisa por ID", response = List.class)
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_CRUD_SISTEMA') and #oauth2.hasScope('read')")
    @Override
    public ResponseEntity<Sistema> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Sistema> sistema = sistemaService.findById(id);
        return sistema.isPresent() ? ResponseEntity.ok(sistema.get()) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Lista dos sistemas ativos - dropdown ", response = List.class)
    @GetMapping(value = "/ativos/dropdown")
    @Override
    public Collection<IDAndNomeGenericoProjection> findByStatusTrue() {
        return sistemaService.findByStatusTrue();
    }

    @ApiOperation(value = "Atualizar o status")
    @PutMapping(value = "/ativo/{id}")
    @PreAuthorize("hasAuthority('ROLE_DESATIVAR_SISTEMA') and #oauth2.hasScope('write')")
    @Override
    public void updatePropertyStatus(@PathVariable(value = "id") Long id, @RequestBody Boolean status)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {
        sistemaService.updatePropertyStatus(id, status);
    }

    @ApiOperation(value = "Excluir sistema - Default : Só o administrador poderá fazer essa exclusão fisica.")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_CRUD_SISTEMA') and #oauth2.hasScope('write')")
    @Override
    public void deleteById(@PathVariable Long id) throws ResourceNotFoundException {
        sistemaService.deleteById(id);
    }

    @ApiOperation(value = "Duas listas : Vinculadas e Não vinculadas ao usuário ", response = List.class)
    @GetMapping(value = "/ativos/picklist")
    @Override
    public GenericoPickListProjection findByPickListProjection(@RequestParam(required = false) Long usuarioID)
            throws ResourceNotFoundException {
        return sistemaService.findBySistemaPickListProjection(usuarioID);
    }

}
