package br.com.obt.sca.api.resource;

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
import br.com.obt.sca.api.projections.GenericoPickListProjection;
import br.com.obt.sca.api.projections.IDAndNomeGenericoProjection;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Collection;
import java.util.Map;

@Api(value = "perfis", description = "Serviço de perfis")
@ApiResponses(value = {
    @ApiResponse(code = 200, message = "Lista de perfis executada com sucesso"),
    @ApiResponse(code = 201, message = "Pefil cadastrado com sucesso"),
    @ApiResponse(code = 301, message = "O recurso que você estava tentando acessar foi encontrado"),
    @ApiResponse(code = 401, message = "Você não está autorizado para visualizar este recurso"),
    @ApiResponse(code = 403, message = "O recurso que você estava tentando acessar é restrito"),
    @ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado")
})
@RestController
@RequestMapping("/perfis")
public class PerfilResource extends BaseResource<Perfil> {

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    protected Specification getSpecificationPaginacao(Map<String, String> map) {
        return Specification.where(new BaseFilter("nome", SearchCriteria.CONTAINS, map))
                .and(new BaseFilter("id", SearchCriteria.EQUALS, getValueMap(map, "sistema.id"), "sistema"))
                .and(new BaseFilter("descricao", SearchCriteria.CONTAINS, map))
                .and(new BaseFilter("status", SearchCriteria.EQUALS, map));
    }

    @ApiOperation(value = "Lista paginada de perfis", response = List.class)
    @GetMapping(value = "/paginacao/{page}/{limit}")
    @PreAuthorize("hasAuthority('ROLE_CRUD_PERFIL')")
    @Override
    public List<Perfil> findAllPaginacao(
            @RequestParam(required = false) Map<String, String> map,
            @PathVariable int page,
            @PathVariable int limit) {

        PageRequest pageRequest = getPageRequestDefault(page, limit, map);
        Specification spec = getSpecificationPaginacao(map);

        return perfilService.findAll(spec, pageRequest).getContent();
    }

    @ApiOperation(value = "Retorna a quantidade de perfis de acordo com os filtros", response = List.class)
    @GetMapping(value = "/count/all/")
    @PreAuthorize("hasAuthority('ROLE_CRUD_PERFIL')")
    @Override
    public Long countAllPaginacao(@RequestParam(required = false) Map<String, String> map) {
        return perfilService.countAll(getSpecificationPaginacao(map));
    }

    @ApiOperation(value = "Salvar uma perfil", response = List.class)
    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_CRUD_PERFIL') and #oauth2.hasScope('write')")
    @Override
    public ResponseEntity<Perfil> save(@Valid @RequestBody Perfil perfil, HttpServletResponse response)
            throws ResourceAlreadyExistsException, ResourceNotFoundException {
        Perfil perfilSalvo = perfilService.save(perfil);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, perfilSalvo.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(perfilSalvo);
    }

    @ApiOperation(value = "Salvar um perfil e suas permissões", response = List.class)
    @PostMapping(value = "/perfil/permissoes")
    @PreAuthorize("hasAuthority('ROLE_CRUD_PERFIL') and #oauth2.hasScope('write')")
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
    @PreAuthorize("hasAuthority('ROLE_CRUD_PERFIL') and #oauth2.hasScope('read')")
    @Override
    public ResponseEntity<Perfil> findById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Perfil> perfil = perfilService.findById(id);
        return perfil.isPresent() ? ResponseEntity.ok(perfil.get()) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Lista dos ativos - dropdown ", response = List.class)
    @GetMapping(value = "/ativos/dropdown")
    @Override
    public Collection<IDAndNomeGenericoProjection> findByStatusTrue() {
        return perfilService.findByStatusTrue();
    }

    @ApiOperation(value = "Atualizar o status")
    @PutMapping(value = "/ativo/{id}")
    @PreAuthorize("hasAuthority('ROLE_DESATIVAR_PERFIL') and #oauth2.hasScope('write')")
    @Override
    public void updatePropertyStatus(@PathVariable Long id, @RequestBody Boolean status)
            throws ResourceNotFoundException, ResourceAlreadyExistsException {
        perfilService.updatePropertyStatus(id, status);
    }

    @ApiOperation(value = " Permissões  ", response = List.class)
    @GetMapping(value = "/ativos/{idUsuario}")
    public List<Perfil> findByPermissoesDoUsuario(@PathVariable Long idUsuario) throws ResourceNotFoundException {
        return perfilService.findByPerfisDoUsuario(idUsuario);
    }

    @ApiOperation(value = "Excluir perfil - Default : Só o administrador poderá fazer essa exclusão fisica.")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_CRUD_PERFIL') and #oauth2.hasScope('write')")
    @Override
    public void deleteById(@PathVariable Long id) throws ResourceNotFoundException {
        perfilService.deleteById(id);
    }

    @ApiOperation(value = "Duas listas : Vinculadas e Não vinculadas ao usuário ", response = List.class)
    @GetMapping(value = "/ativos/picklist")
    @Override
    public GenericoPickListProjection findByPickListProjection(
            @RequestParam(required = false) Long usuarioid) throws ResourceNotFoundException {
        return perfilService.findByPerfilPickListProjection(usuarioid);
    }
}
