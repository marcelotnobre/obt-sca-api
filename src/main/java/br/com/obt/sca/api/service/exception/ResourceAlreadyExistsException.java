package br.com.obt.sca.api.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FOUND)
public class ResourceAlreadyExistsException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public ResourceAlreadyExistsException(String message) {
		super(message);
	}

	public ResourceAlreadyExistsException(Exception exception, String message) {
		super(exception);
	}

	public ResourceAlreadyExistsException(Exception exception) {
		super(exception);
	}

}
