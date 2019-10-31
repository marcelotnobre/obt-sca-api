package br.com.obt.sca.api.resource.filter;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Luiz Eduardo
 */
@Getter
@Setter
public class SearchCriteria {

    private String key;
    private String operation;
    private Object value;

    public SearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

}
