package br.com.obt.sca.api.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FOUND)
public class ResourceAdministratorNotUpdateException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public ResourceAdministratorNotUpdateException(String message) {
		super(message);
	}

	public ResourceAdministratorNotUpdateException(Exception exception, String message) {
		super(exception);
	}

	public ResourceAdministratorNotUpdateException(Exception exception) {
		super(exception);
	}

}
