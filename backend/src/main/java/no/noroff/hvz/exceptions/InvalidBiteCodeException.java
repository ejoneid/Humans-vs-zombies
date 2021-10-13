package no.noroff.hvz.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidBiteCodeException extends ResponseStatusException {
    public InvalidBiteCodeException(String msg) {
        super(HttpStatus.BAD_REQUEST, msg);
    }
}
