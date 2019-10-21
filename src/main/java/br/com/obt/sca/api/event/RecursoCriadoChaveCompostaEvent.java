package br.com.obt.sca.api.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

public class RecursoCriadoChaveCompostaEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	private HttpServletResponse response;
	@Getter
	private Long codigo1;
	@Getter
	private Long codigo2;

	public RecursoCriadoChaveCompostaEvent(Object source, HttpServletResponse response, Long codigo1, Long codigo2) {
		super(source);
		this.response = response;
		this.codigo1 = codigo1;
		this.codigo2 = codigo2;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

}
