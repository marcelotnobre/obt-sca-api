package br.com.obt.sca.api.exceptionhandler;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.obt.sca.api.config.SQLIntegrityConstraintViolationConfig;
import br.com.obt.sca.api.service.exception.ResourceAdministratorNotUpdateException;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceFileStorageException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ResourceParameterNullException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    protected SQLIntegrityConstraintViolationConfig SQLIntegrityMessage;

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        String mensagemUser = "Parâmetro " + ex.getParameterName() + " Faltando.";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, mensagemUser, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" Tipo de mídia não é suportado. Tipos de mídia suportados são: ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(
                new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
    }

//	@ExceptionHandler(RateLimitExceededException.class)
//	public ResponseEntity<Object> handleRateLimitExceededException(RateLimitExceededException ex) {
//		String mensagemUser = ex.getProviderId();
//		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, mensagemUser, ex));
//	}
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        logger.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String mensagemUser = "Requisição JSON mal formada";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, mensagemUser, ex));

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);

        String mensagemUser = messageSource.getMessage("violacao.regra-excecao", null, LocaleContextHolder.getLocale());
        apiError.setMessageUser(mensagemUser);

        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);

    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        String mensagemUsuario = "Erro ao gravar saída do Json.";
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, mensagemUsuario, ex));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Object> handleMultipartException(HttpServletRequest req, MultipartException ex)
            throws Exception {
        String mensagemUsuario = "Favor anexar ao menos um arquivo.";
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, mensagemUsuario, ex));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxUploadSizeExceededException(HttpServletRequest req,
            MaxUploadSizeExceededException ex) throws Exception {
        String error = "Tamanho máximo do arquivo excedido.";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
            WebRequest request) {
        String mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null,
                LocaleContextHolder.getLocale());
        if (ex.getCause() instanceof ConstraintViolationException) {

            String messageSqlException = ((ConstraintViolationException) ex.getCause()).getSQLException().getMessage();

            int indice_abre = messageSqlException.indexOf("'");
            int indice_fecha = messageSqlException.trim().substring(indice_abre + 1).indexOf("'");
            String nomeCampo = messageSqlException.substring(indice_abre + 1, indice_abre + indice_fecha + 1);
            SQLIntegrityConstraintViolationConfig.nomeCampo = nomeCampo;

            SQLIntegrityMessage.initHashMapMessageException();
            String nomeConstraint = ((ConstraintViolationException) ex.getCause()).getConstraintName();
            mensagemUsuario = SQLIntegrityMessage.getHashMapMessageException().get(nomeConstraint);

//			if (ExceptionUtils.getRootCauseMessage(ex).contains("usuario_permissao")) {
//				logger.info("Erro de constraint : {} ",ExceptionUtils.getRootCauseMessage(ex));
//				mensagemUsuario = "Não pode excluir ou atualizar essa permissão. Permissão sendo utilizada. ";
//				return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, mensagemUsuario, ex.getCause()));
//			}
//			return buildResponseEntity(new ApiError(HttpStatus.FOUND, mensagemUsuario, ex.getCause()));
            return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, mensagemUsuario, ex.getCause()));
        }
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, mensagemUsuario, ex));

    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);

        String mensagemUsuario = messageSource.getMessage("violacao.regra-excecao", null,
                LocaleContextHolder.getLocale());
        apiError.setMessageUser(mensagemUsuario);

        apiError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(javax.validation.UnexpectedTypeException.class)
    protected ResponseEntity<Object> handleUnexpectedType(javax.validation.UnexpectedTypeException ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        apiError.setMessageUser(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        String mensagemUsuario = messageSource.getMessage("metodo.argumento-tipo-incompativel-excecao", null,
                LocaleContextHolder.getLocale());

        if (ex != null && ex.getRequiredType() != null) {
            Class<?> requiredType = ex.getRequiredType();
            Object value = ex.getValue();
            if (requiredType != null && requiredType.getSimpleName() != null) {
                String simpleName = requiredType.getSimpleName();
//				apiError.setMessageUser(String.format(mensagemUsuario, ex.getName(), ex.getValue(),requiredType.getSimpleName()));
                apiError.setMessageUser(String.format(mensagemUsuario, ex.getName(), value, simpleName));
            }
        }

        return buildResponseEntity(apiError);
    }

    /**
     * Handle failures commonly thrown from code
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleMiscFailures(IllegalArgumentException ex) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        String mensagemUser = messageSource.getMessage("ilegal.argument-exception", null,
                LocaleContextHolder.getLocale());
        apiError.setMessageUser(mensagemUser);

        apiError.setMessageUser(mensagemUser);
        return buildResponseEntity(apiError);
    }

    // Erros criados
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex);
        apiError.setMessageUser(ex.getMessage());
        apiError.setMessageDeveloper("ResourceNotFoundException . " + ex.getMessage());
        return buildResponseEntity(apiError);

    }

    @ExceptionHandler(ResourceFileStorageException.class)
    public ResponseEntity<?> resourceResourceFileStorageException(ResourceFileStorageException ex, WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex);
        apiError.setMessageUser(ex.getMessage());
        apiError.setMessageDeveloper("ResourceFileStorageException . " + ex.getMessage());
        return buildResponseEntity(apiError);

    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<?> resourceAlreadyRegisteredException(ResourceAlreadyExistsException ex, WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.FOUND, ex);
        apiError.setMessageUser(ex.getMessage());
        apiError.setMessageDeveloper("AlreadyRegisteredException . " + ex.getMessage());
        return buildResponseEntity(apiError);

    }

    @ExceptionHandler(ResourceAdministratorNotUpdateException.class)
    public ResponseEntity<?> resourceAdministratorNotUpdateException(ResourceAdministratorNotUpdateException ex,
            WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.FOUND, ex);
        apiError.setMessageUser(ex.getMessage());
        apiError.setMessageDeveloper("ResourceAdministratorNotUpdateException . " + ex.getMessage());
        return buildResponseEntity(apiError);

    }

    @ExceptionHandler(ResourceParameterNullException.class)
    public ResponseEntity<?> resourceParameterNullException(ResourceParameterNullException ex, WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex);
        apiError.setMessageUser(ex.getMessage());
        apiError.setMessageDeveloper("ResourceParameterNullException . " + ex.getMessage());
        return buildResponseEntity(apiError);

    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex);
        apiError.setMessageUser(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
