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

    public static final String EQUALS = "equals";
    public static final String CONTAINS = "contains";
    public static final String START_WITH = "startWith";
    public static final String END_WITH = "endWith";

    private String chave;
    private String operacao;
    private Object valor;
    private String join;

    public SearchCriteria(String chave, String operacao, Object valor) {
        this.chave = chave;
        this.operacao = operacao;
        this.valor = valor;
    }

    public SearchCriteria(String chave, String operacao, Object valor, String join) {
        this.chave = chave;
        this.operacao = operacao;
        this.valor = valor;
        this.join = join;
    }

}
