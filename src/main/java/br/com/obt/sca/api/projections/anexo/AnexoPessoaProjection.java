package br.com.obt.sca.api.projections.anexo;

public interface AnexoPessoaProjection {

	Long getId();

	String getLabel();

	Long getTamanho();

	String getTipoConteudo();

	byte[] getLob();
}
