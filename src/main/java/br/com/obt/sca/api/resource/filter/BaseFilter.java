package br.com.obt.sca.api.resource.filter;

import br.com.obt.sca.api.model.Usuario;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.criteria.CriteriaQuery;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author Luiz Eduardo
 */
public class BaseFilter<T> implements Specification<T> {

    private SearchCriteria criteria;

    public BaseFilter(String chave, String operacao, Object valor) {
        this.criteria = new SearchCriteria(chave, operacao, valor);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getValor() != null) {
            if (root.get(criteria.getChave()).getJavaType() == String.class) {
                if (criteria.getOperacao().equalsIgnoreCase(SearchCriteria.CONTAINS)) {
                    return builder.like(root.<String>get(criteria.getChave()), "%" + criteria.getValor() + "%");
                } else if (criteria.getOperacao().equalsIgnoreCase(SearchCriteria.START_WITH)) {
                    return builder.like(root.<String>get(criteria.getChave()), "%" + criteria.getValor());
                } else if (criteria.getOperacao().equalsIgnoreCase(SearchCriteria.END_WITH)) {
                    return builder.like(root.<String>get(criteria.getChave()), criteria.getValor() + "%");
                } else if (criteria.getOperacao().equalsIgnoreCase(SearchCriteria.EQUALS)) {
                    return builder.equal(root.get(criteria.getChave()), "" + criteria.getValor());
                }
            } else if (root.get(criteria.getChave()).getJavaType() == Boolean.class) {
                return builder.equal(root.get(criteria.getChave()), criteria.getValor());
            }
        }
        return null;
    }

}
