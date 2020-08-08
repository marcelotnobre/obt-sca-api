package br.com.obt.sca.api.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceParameterNullException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public ResourceParameterNullException(String message) {
        super(message);
    }

    public ResourceParameterNullException(Exception exception, String message) {
        super(message, exception);
    }

    public ResourceParameterNullException(Exception exception) {
        super(exception);
    }

}
