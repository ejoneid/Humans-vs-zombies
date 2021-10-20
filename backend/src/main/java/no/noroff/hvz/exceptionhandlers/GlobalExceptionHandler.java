package no.noroff.hvz.exceptionhandlers;

import no.noroff.hvz.exceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNoSuchElementException(NullPointerException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidBiteCodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidBiteCodeException(InvalidBiteCodeException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppUserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidBiteCodeException(AppUserNotFoundException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPermissionsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleInvalidBiteCodeException(MissingPermissionsException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AppUserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleInvalidBiteCodeException(AppUserAlreadyExistException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MissingPlayerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleInvalidBiteCodeException(MissingPlayerException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleInvalidBiteCodeException(PlayerAlreadyExistException e, WebRequest r) {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
