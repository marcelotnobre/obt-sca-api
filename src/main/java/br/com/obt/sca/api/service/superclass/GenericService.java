package br.com.obt.sca.api.service.superclass;
 
import br.com.obt.sca.api.projections.IDAndNomeGenericoProjection;
import br.com.obt.sca.api.repository.superclass.GenericRepository;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.util.ReflectionUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Luiz Eduardo
 * @param <T>
 */
public abstract class GenericService<T> {

    protected static final Logger logger = LoggerFactory.getLogger(GenericService.class);

    protected final GenericRepository<T, Long> repository;

    public GenericService(GenericRepository<T, Long> repo) {
        this.repository = repo;
    }

    public abstract Optional<T> updateFields(Optional<T> beanBanco, T bean);

    public Optional<T> findById(Long id) throws ResourceNotFoundException {
        return repository.findById(id);
    }

    @Transactional(readOnly = false)
    public void deleteById(Long id) throws ResourceNotFoundException {
        validateFindByIdExists(id);
        repository.deleteById(id);
    }

    public void validateFindByIdExists(Long id) throws ResourceNotFoundException {
        Optional<T> beanBanco = findById(id);
        if (!beanBanco.isPresent()) {
            throw new ResourceNotFoundException("O código " + id + " não foi encontrado. ");
        }
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = false)
    public void updatePropertyStatus(Long id, Boolean status) throws ResourceNotFoundException, ResourceAlreadyExistsException {
        validateFindByIdExists(id);
        Optional<T> beanBanco = findById(id);
        T bean = beanBanco.get();
        try {
            bean = (T) ReflectionUtil.setStatusByReflection(bean, status);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
        save(bean);
    }

    @Transactional(readOnly = false)
    public T save(T bean) throws ResourceAlreadyExistsException, ResourceNotFoundException {
        if (bean != null) {
            Long id = ReflectionUtil.getIdByReflection(bean);
            if (id != null) {
                Optional<T> beanBanco = findById(id);
                if (!beanBanco.isPresent()) {
                    beanBanco = updateFields(beanBanco, bean);
                    BeanUtils.copyProperties(beanBanco.get(), bean, "id");
                }
            }
            T beanSalvo = repository.save(bean);
            logger.info("save - Sucesso - Parametros {}.", beanSalvo.toString());
            return beanSalvo;
        } else {
            throw new ResourceNotFoundException("Dados não encontrados!.");
        }
    }

    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    public Long countAll(Specification<T> spec) {
        return repository.count(spec);
    }

    public Collection<IDAndNomeGenericoProjection> findByStatusTrue() {
        return repository.findByStatusTrue(IDAndNomeGenericoProjection.class);
    }
}
