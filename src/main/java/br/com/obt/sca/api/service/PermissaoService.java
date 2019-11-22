package br.com.obt.sca.api.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.obt.sca.api.model.Permissao;
import br.com.obt.sca.api.projections.GenericoPinkListProjection;
import br.com.obt.sca.api.projections.IDAndNomeGenericoProjection;
import br.com.obt.sca.api.repository.PermissaoRepository;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ServiceException;
import org.springframework.data.jpa.domain.Specification;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
@Service
public class PermissaoService {

    // private static final Logger logger =
    // LoggerFactory.getLogger(PermissaoService.class);
    @Autowired
    private PermissaoRepository permissaoRepository;

    @Transactional(readOnly = false)
    public Permissao save(Permissao permissao) throws ResourceAlreadyExistsException, ResourceNotFoundException {

        if ((permissao != null) && (permissao.getId() != null)) {
            Optional<Permissao> permissaoBanco = this.findById(permissao.getId());
            // Setando os campos que são alterados na tela.
            permissaoBanco.get().setNome(permissao.getNome());
            permissaoBanco.get().setDescricao(permissao.getDescricao());
            permissaoBanco.get().setStatus(permissao.getStatus());
            permissaoBanco.get().setSistema(permissao.getSistema());
            BeanUtils.copyProperties(permissaoBanco.get(), permissao, "id");
        }

        if (permissao != null) {
            return permissaoRepository.save(permissao);
        }
        return permissao;

    }

    @Transactional(readOnly = false)
    public void updatePropertyStatus(Long id, Boolean status) throws ResourceNotFoundException {
        Optional<Permissao> permissaoBanco = findById(id);

        Permissao permissao = permissaoBanco.get();
        permissao.setStatus(status);
        permissaoRepository.save(permissao);
    }

    @Transactional(readOnly = false)
    public void deleteById(Long id) throws ResourceNotFoundException {

        validatefindByIdExists(id);
        permissaoRepository.deleteById(id);

    }

    public Page<Permissao> findAll(Specification<Permissao> spec, Pageable pageable) {
        return permissaoRepository.findAll(spec, pageable);
    }

    public Long countAll(Specification<Permissao> spec) {
        return permissaoRepository.count(spec);
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

    public GenericoPinkListProjection findByPermissaoPinkListProjection(Long perfilid) {

        GenericoPinkListProjection permissoesPinkListProjection = new GenericoPinkListProjection();

        Collection<IDAndNomeGenericoProjection> permissoesSelecionadas = this
                .findByPermissaoPerfilSelectedStatusTrue(perfilid);
        Collection<IDAndNomeGenericoProjection> permissoesDisponiveis = this
                .findByPermissaoPerfilAvailableStatusTrue(perfilid);

        permissoesPinkListProjection.setRegistrosDisponiveis(permissoesDisponiveis);
        permissoesPinkListProjection.setRegistrosSelecionados(permissoesSelecionadas);

        return permissoesPinkListProjection;
    }

    public Optional<Permissao> findById(Long id) throws ResourceNotFoundException {
        Optional<Permissao> permissaoBanco = permissaoRepository.findById(id);
        if (!permissaoBanco.isPresent()) {
            throw new ResourceNotFoundException("O código " + id + " da permissão não foi encontrado. ");
        }
        return permissaoBanco;
    }

    public List<Permissao> findByPermissoesDoUsuario(Long idUsuario) throws ResourceNotFoundException {
        List<Permissao> permissaoBanco = permissaoRepository.findByPermissoesDoUsuario(idUsuario);
        if (permissaoBanco.isEmpty()) {
            throw new ResourceNotFoundException("O código " + idUsuario + " do usuário não foi encontrado. ");
        }
        return permissaoBanco;
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

    // Metodos Privados
    private void validatefindByIdExists(Long id) throws ResourceNotFoundException {
        Optional<Permissao> permissaoBanco = this.findById(id);
        if (!permissaoBanco.isPresent()) {
            throw new ResourceNotFoundException("O código " + id + " da permissão não foi encontrado. ");
        }
    }

}
