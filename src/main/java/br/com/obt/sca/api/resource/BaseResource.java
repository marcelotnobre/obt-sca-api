package br.com.obt.sca.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import br.com.obt.sca.api.projections.GenericoPickListProjection;
import br.com.obt.sca.api.projections.IDAndNomeGenericoProjection;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import java.util.Collection;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class BaseResource<T> {

    protected String getValueMap(Map<String, String> map, String chave) {
        if (map != null && map.containsKey(chave)) {
            return map.get(chave);
        }
        return null;
    }

    protected PageRequest getPageRequestDefault(int page, int limit, Map<String, String> map) {
        String sort = getValueMap(map, "sort");
        String order = getValueMap(map, "order");

        return PageRequest.of(page, limit,
                Sort.by("asc".equals(order) ? Sort.Direction.ASC : Sort.Direction.DESC, sort));
    }

    protected abstract Specification getSpecificationPaginacao(Map<String, String> map);

    protected abstract List<T> findAllPaginacao(
            @RequestParam(required = true) Map<String, String> map,
            @PathVariable int page,
            @PathVariable int limit);

    protected abstract Long countAllPaginacao(@RequestParam(required = true) Map<String, String> map);

    protected abstract ResponseEntity<T> save(@Valid
            @RequestBody T bean, HttpServletResponse response)
            throws ResourceAlreadyExistsException, ResourceNotFoundException;

    protected abstract ResponseEntity<T> findById(@PathVariable Long id) throws ResourceNotFoundException;

    protected abstract Collection<IDAndNomeGenericoProjection> findByStatusTrue();

    protected abstract void updatePropertyStatus(@PathVariable Long id, @RequestBody Boolean status)
            throws ResourceNotFoundException, ResourceAlreadyExistsException;

    protected abstract void deleteById(@PathVariable Long id) throws ResourceNotFoundException;

    protected GenericoPickListProjection findByPickListProjection(
            @RequestParam(required = false) Long id) throws ResourceNotFoundException {
        return new GenericoPickListProjection();
    }

}
