package br.com.obt.sca.api.model.enumeration;

public enum TipoAutenticacao {

    CERTIFICADO("Certificado"), LDAP("Ldap"), SCA("SCA");

    private final String label;

    public String getLabel() {
        return label;
    }

    private TipoAutenticacao(String label) {
        this.label = label;
    }
}
