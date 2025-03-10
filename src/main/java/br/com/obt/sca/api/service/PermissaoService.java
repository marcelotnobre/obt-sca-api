package br.com.obt.sca.api.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import br.com.obt.sca.api.model.Permissao;
import br.com.obt.sca.api.projections.GenericoPickListProjection;
import br.com.obt.sca.api.projections.IDAndNomeGenericoProjection;
import br.com.obt.sca.api.repository.PermissaoRepository;
import br.com.obt.sca.api.repository.superclass.GenericRepository;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ServiceException;
import br.com.obt.sca.api.service.superclass.GenericService;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
@Service
public class PermissaoService extends GenericService<Permissao> {

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PerfilService perfilService;

    @Autowired
    public PermissaoService(GenericRepository<Permissao, Long> repository) {
        super(repository);
    }

    @Override
    public Optional<Permissao> updateFields(Optional<Permissao> permissaoBanco, Permissao permissao) {
        permissaoBanco.get().setNome(permissao.getNome());
        permissaoBanco.get().setDescricao(permissao.getDescricao());
        permissaoBanco.get().setStatus(permissao.getStatus());
        permissaoBanco.get().setSistema(permissao.getSistema());
        return permissaoBanco;
    }

    public Page<Permissao> findByNomeContainingAndStatusEquals(String nome, Boolean status, Pageable pageable) {
        return permissaoRepository.findByNomeContainingAndStatusEquals(nome, status, pageable);
    }

    public Page<Permissao> findByNomeContainingAndSistemaAndStatus(Long sistemaid, String nome, Boolean status,
            Pageable pageable) {
        return permissaoRepository.findByNomeContainingAndSistemaAndStatus(sistemaid, nome, status, pageable);
    }

    public Collection<IDAndNomeGenericoProjection> findByPermissaoPerfilAvailableStatusTrue(Long perfilid) {
        return permissaoRepository.findByPermissaoPerfilAvailableStatusTrue(perfilid,
                IDAndNomeGenericoProjection.class);
    }

    public Collection<IDAndNomeGenericoProjection> findByPermissaoPerfilSelectedStatusTrue(Long perfilid) {
        return permissaoRepository.findByPermissaoPerfilSelectedStatusTrue(perfilid, IDAndNomeGenericoProjection.class);
    }

    public GenericoPickListProjection findByPermissaoPickListProjection(Long perfilid) {

        GenericoPickListProjection permissoesPickListProjection = new GenericoPickListProjection();

        Collection<IDAndNomeGenericoProjection> permissoesSelecionadas = this
                .findByPermissaoPerfilSelectedStatusTrue(perfilid);
        Collection<IDAndNomeGenericoProjection> permissoesDisponiveis = this
                .findByPermissaoPerfilAvailableStatusTrue(perfilid);

        permissoesPickListProjection.setRegistrosDisponiveis(permissoesDisponiveis);
        permissoesPickListProjection.setRegistrosSelecionados(permissoesSelecionadas);

        return permissoesPickListProjection;
    }

    public GenericoPickListProjection findByUsuarioPickListProjection(Long usuarioID) throws ResourceNotFoundException {
        if (usuarioID != null) {
            usuarioService.validateFindByIdExists(usuarioID);
        }

        Collection<IDAndNomeGenericoProjection> registrosDisponiveis = new ArrayList<>();
        Collection<IDAndNomeGenericoProjection> registrosSelecionados = new ArrayList<>();
        
        if(usuarioID == null) {
        	registrosDisponiveis = permissaoRepository.findDisponiveisByNovoUsuario(IDAndNomeGenericoProjection.class);
        } else {        	
        	List<Long> idPermissoes = permissaoRepository.findByUsuarioJoinUsuarioPerfil(usuarioID).stream()
        			.map(Permissao::getId).collect(Collectors.toList());
        	
        	if(idPermissoes.isEmpty()) {
        		registrosDisponiveis = permissaoRepository.findDisponiveisByUsuarioJoinUsuarioPermissao(usuarioID, IDAndNomeGenericoProjection.class);
        	} else {        		
        		registrosDisponiveis = permissaoRepository.findDisponiveisByUsuarioJoinUsuarioPermissao(usuarioID, idPermissoes, IDAndNomeGenericoProjection.class);
        	}
        	
        	registrosSelecionados = permissaoRepository.findSelecionadasByUsuarioJoinUsuarioPermissao(usuarioID, IDAndNomeGenericoProjection.class);
        	registrosDisponiveis.removeAll(registrosSelecionados);
        }


        GenericoPickListProjection permissoesPickListProjection = new GenericoPickListProjection();
        
		permissoesPickListProjection.setRegistrosDisponiveis(registrosDisponiveis);        
		permissoesPickListProjection.setRegistrosSelecionados(registrosSelecionados);
        return permissoesPickListProjection;
    }

    public List<Permissao> findByPermissoesDoUsuario(Long idUsuario) throws ResourceNotFoundException {
        List<Permissao> permissoes = new ArrayList<>();
        List<Permissao> permissoesPerfilPermissao = permissaoRepository.findByUsuarioJoinUsuarioPerfil(idUsuario);
        List<Permissao> permissoesUsuarioPermissao = permissaoRepository.findByUsuarioJoinUsuarioPermissao(idUsuario);

        permissoes.addAll(permissoesPerfilPermissao);
        permissoes.addAll(permissoesUsuarioPermissao);
        return permissoes;
    }

    public List<Permissao> findByPermissoesDoPerfil(Long idPerfil) throws ResourceNotFoundException {
        perfilService.validateFindByIdExists(idPerfil);
        return permissaoRepository.findByPermissoesDoPerfil(idPerfil);
    }

    public List<Permissao> findPermissoes(String nomePermissao, Long idUsuario) throws ResourceNotFoundException {
        List<Permissao> permissoes = permissaoRepository.findPermissoes(nomePermissao, idUsuario);
        if (permissoes.isEmpty()) {
            throw new ResourceNotFoundException("Permissão " + nomePermissao + " do usuário não foi encontrada. ");
        }
        return permissoes;
    }

    public Permissao findPermissaoPorSistema(String nomePermissao, Long idSistema) throws ResourceNotFoundException {
        Permissao permissao = permissaoRepository.findPermissaoPorSistema(nomePermissao, idSistema);
        if (permissao == null) {
            throw new ResourceNotFoundException("Permissão " + nomePermissao + " por sistema não foi encontrada. ");
        }
        return permissao;
    }
}
