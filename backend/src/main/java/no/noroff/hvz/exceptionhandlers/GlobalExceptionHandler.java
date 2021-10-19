package no.noroff.hvz.exceptionhandlers;

import no.noroff.hvz.exceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NullPointerException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidBiteCodeException.class)
    public ResponseEntity<Object> handleInvalidBiteCodeException(InvalidBiteCodeException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppUserNotFoundException.class)
    public ResponseEntity<Object> handleInvalidBiteCodeException(AppUserNotFoundException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPermissionsException.class)
    public ResponseEntity<Object> handleInvalidBiteCodeException(MissingPermissionsException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AppUserAlreadyExistException.class)
    public ResponseEntity<Object> handleInvalidBiteCodeException(AppUserAlreadyExistException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MissingPlayerException.class)
    public ResponseEntity<Object> handleInvalidBiteCodeException(MissingPlayerException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerAlreadyExistException.class)
    public ResponseEntity<Object> handleInvalidBiteCodeException(PlayerAlreadyExistException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
