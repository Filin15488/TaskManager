package by.Filin.TaskManager.advice;

import by.Filin.TaskManager.DTO.exception.ExceptionResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .message(ex.getMessage())
                .build()
                ,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .message(ex.getMessage())
                .build()
                ,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .message(e.getMessage())
                        .build()
                ,HttpStatus.CONFLICT);
    }


    @ExceptionHandler(JpaObjectRetrievalFailureException.class)
    public ResponseEntity<?> handleException(JpaObjectRetrievalFailureException e) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .message(e.getMessage())
                        .build()
                ,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleException(AccessDeniedException e) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
//                        .message(e.getMessage())
                        .build()
                ,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleEntityExistsException (EntityExistsException e) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .message(e.getMessage())
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = "";
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMessage += fieldError.getDefaultMessage() + " ";
        }
//        обрежем последний пробел
        errorMessage = errorMessage.substring(0, errorMessage.length() - 1);

        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .message(errorMessage)
                        .build()
                ,HttpStatus.CONFLICT
        );

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .message(ex.getMessage())
                        .build()
                ,HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
