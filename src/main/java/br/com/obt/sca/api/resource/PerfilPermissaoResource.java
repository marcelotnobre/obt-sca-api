package br.com.obt.sca.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.obt.sca.api.event.RecursoCriadoChaveCompostaEvent;
import br.com.obt.sca.api.model.PerfilPermissao;
import br.com.obt.sca.api.service.PerfilPermissaoService;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ResourceParameterNullException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "perfil permissao", description = "Serviço de perfil permissao")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista de perfis executada com sucesso"),
		@ApiResponse(code = 201, message = "Pefil Permissão cadastrado com sucesso"),
		@ApiResponse(code = 301, message = "O recurso que você estava tentando acessar foi encontrado"),
		@ApiResponse(code = 401, message = "Você não está autorizado para visualizar este recurso"),
		@ApiResponse(code = 403, message = "O recurso que você estava tentando acessar é restrito"),
		@ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado") })
@RestController
@RequestMapping("/perfipermissao")
public class PerfilPermissaoResource {

	@Autowired
	private PerfilPermissaoService perfilPermissaoService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@ApiOperation(value = "Salvar uma perfil permissao", response = List.class)
	@PostMapping()
	@PreAuthorize("hasAuthority('ROLE_CRUD_PERFIL') and #oauth2.hasScope('write')")
	public ResponseEntity<PerfilPermissao> save(@Valid @RequestBody PerfilPermissao perfilPermissao,
			HttpServletResponse response)
			throws ResourceAlreadyExistsException, ResourceNotFoundException, ResourceParameterNullException {
		PerfilPermissao perfilPermissaoSalvo = perfilPermissaoService.save(perfilPermissao);

		Long idPerfil = perfilPermissaoSalvo.getPerfilPermissaoPK().getPerfil().getId();
		Long idPermissao = perfilPermissaoSalvo.getPerfilPermissaoPK().getPermissao().getId();

		publisher.publishEvent(new RecursoCriadoChaveCompostaEvent(this, response, idPerfil, idPermissao));
		return ResponseEntity.status(HttpStatus.CREATED).body(perfilPermissaoSalvo);
	}

	@ApiOperation(value = "Pesquisa por ID de perdil e ID de Permissao", response = List.class)
	@GetMapping(value = "/perfil/{idperfil}/permissao/{idpermissao}")
	@PreAuthorize("hasAuthority('ROLE_CRUD_PERFIL') and #oauth2.hasScope('read')")
	public ResponseEntity<PerfilPermissao> findById(@PathVariable Long idperfil, @PathVariable Long idpermissao)
			throws ResourceNotFoundException {
		Optional<PerfilPermissao> perfilPermissao = perfilPermissaoService.findByIdPerfilIdPermissao(idperfil,
				idpermissao);
		return perfilPermissao.isPresent() ? ResponseEntity.ok(perfilPermissao.get())
				: ResponseEntity.notFound().build();
	}

	@ApiOperation(value = "Excluir perfil Permissao - Default : Só o administrador poderá fazer essa exclusão fisica.")
	@DeleteMapping(value = "/perfil/{idperfil}/permissao/{idpermissao}")
	@PreAuthorize("hasAuthority('ROLE_CRUD_PERFIL') and #oauth2.hasScope('write')")
	public void deleteById(@PathVariable Long idperfil, @PathVariable Long idpermissao)
			throws ResourceNotFoundException, ResourceParameterNullException {
		perfilPermissaoService.deleteByPerfilPermissao(idperfil, idpermissao);
	}

}
