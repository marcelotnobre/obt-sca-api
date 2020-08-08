package br.com.obt.sca.api.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
public class SQLIntegrityConstraintViolationConfig {

    @Getter
    private Map<String, String> hashMapMessageException = new HashMap<String, String>();

    public SQLIntegrityConstraintViolationConfig() {
    }

    public SQLIntegrityConstraintViolationConfig(String nomeCampo) {
        this.initHashMapMessageException(nomeCampo);
    }

    public void initHashMapMessageException(String nomeCampo) {
        // Anexo

        hashMapMessageException.put("UK_ANEXO_NOME_TAM_TPCONTEUDO",
                "O arquivo" + nomeCampo + " já foi salvo!");

        // Atributo
        hashMapMessageException.put("UK_ATRIBUTO_NOME",
                "O atributo " + nomeCampo + " já foi cadastrado! ");

        // Dominio
        hashMapMessageException.put("UK_DOMINIO_NOME",
                "O domínio " + nomeCampo + " já foi cadastrado! ");

        // Perfil
        hashMapMessageException.put("UK_PERFIL_NOME",
                "O perfil  " + nomeCampo + " já foi cadastrado! ");

        // Permissao
        hashMapMessageException.put("UK_PERMISSAO_NOME",
                "A permissão  " + nomeCampo + " já foi cadastrada!. ");

        // Pessoa
        hashMapMessageException.put("UK_PESSOA_NOME",
                "A pessoa " + nomeCampo + " já foi cadastrada! ");

        // Sistema
        hashMapMessageException.put("UK_SISTEMA_NOME",
                "O sistema " + nomeCampo + " já foi cadastrado. ");

        // Usuário
        hashMapMessageException.put("UK_USUARIO_EMAIL",
                "O E-Mail " + nomeCampo + " do usuário já foi cadastrado! ");
        hashMapMessageException.put("UK_USUARIO_LOGIN",
                "O login  " + nomeCampo + " do usuário já foi cadastrado! ");

    }

}
