package br.com.obt.sca.api.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class ResourceFileStorageException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public ResourceFileStorageException(String message) {
        super(message);
    }

    public ResourceFileStorageException(String message, Exception exception) {
        super(message, exception);
    }

    public ResourceFileStorageException(Exception exception) {
        super(exception);
    }

}
