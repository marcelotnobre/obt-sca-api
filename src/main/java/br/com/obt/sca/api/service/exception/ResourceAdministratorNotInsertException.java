package br.com.obt.sca.api.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FOUND)
public class ResourceAdministratorNotInsertException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public ResourceAdministratorNotInsertException(String message) {
		super(message);
	}

	public ResourceAdministratorNotInsertException(Exception exception, String message) {
		super(exception);
	}

	public ResourceAdministratorNotInsertException(Exception exception) {
		super(exception);
	}

}
