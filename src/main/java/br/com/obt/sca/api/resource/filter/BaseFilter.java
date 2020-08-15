package br.com.obt.sca.api.resource.filter;

import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
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

    public BaseFilter(String chave, String operacao, Map<String, Object> map) {
        this.criteria = new SearchCriteria(chave, operacao, map != null && map.get(chave) != null ? map.get(chave) : null);
    }

    public BaseFilter(String chave, String operacao, Map<String, Object> map, String join) {
        this.criteria = new SearchCriteria(chave, operacao, map != null && map.get(chave) != null ? map.get(chave) : null, join);
    }

    public BaseFilter(String chave, String operacao, Object valor, String join) {
        this.criteria = new SearchCriteria(chave, operacao, valor, join);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getValor() != null) {

            if (criteria.getJoin() != null) {

                Path path = root.join(criteria.getJoin());
                Path pathCampo = path.get(criteria.getChave());

                if (root.get(criteria.getChave()).getJavaType() == String.class) {
                    if (criteria.getOperacao().equalsIgnoreCase(SearchCriteria.CONTAINS)) {
                        return builder.like(pathCampo, "%" + criteria.getValor() + "%");
                    } else if (criteria.getOperacao().equalsIgnoreCase(SearchCriteria.START_WITH)) {
                        return builder.like(pathCampo, "%" + criteria.getValor());
                    } else if (criteria.getOperacao().equalsIgnoreCase(SearchCriteria.END_WITH)) {
                        return builder.like(pathCampo, criteria.getValor() + "%");
                    } else if (criteria.getOperacao().equalsIgnoreCase(SearchCriteria.EQUALS)) {
                        return builder.equal(pathCampo, criteria.getValor());
                    }
                } else if (root.get(criteria.getChave()).getJavaType() == Boolean.class) {
                    if (criteria.getValor() instanceof String) {
                        String valor = (String) criteria.getValor();

                        if ("0".equals(valor) || "1".equals(valor)) {
                            return builder.equal(pathCampo, "1".equals(valor) ? Boolean.TRUE : Boolean.FALSE);
                        }
                        return builder.equal(pathCampo, Boolean.parseBoolean(valor));
                    }
                    return builder.equal(pathCampo, criteria.getValor());
                } else if (root.get(criteria.getChave()).getJavaType() == Long.class) {
                    if (criteria.getValor() instanceof String) {
                        return builder.equal(pathCampo, Long.parseLong((String) criteria.getValor()));
                    }
                    return builder.equal(pathCampo, criteria.getValor());
                }
                //FIM JOIN
            } else if (root.get(criteria.getChave()).getJavaType() == String.class) {
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
                if (criteria.getValor() instanceof String) {
                    String valor = (String) criteria.getValor();

                    if ("0".equals(valor) || "1".equals(valor)) {
                        return builder.equal(root.get(criteria.getChave()), "1".equals(valor) ? Boolean.TRUE : Boolean.FALSE);
                    }
                    return builder.equal(root.get(criteria.getChave()), Boolean.parseBoolean(valor));
                }
                return builder.equal(root.get(criteria.getChave()), criteria.getValor());
            } else if (root.get(criteria.getChave()).getJavaType() == Long.class) {
                if (criteria.getValor() instanceof String) {
                    return builder.equal(root.get(criteria.getChave()), Long.parseLong((String) criteria.getValor()));
                }
                return builder.equal(root.get(criteria.getChave()), criteria.getValor());
            }

        }
        return null;
    }
    
}
