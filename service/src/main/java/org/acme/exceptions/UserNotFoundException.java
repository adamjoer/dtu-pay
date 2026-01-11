package org.acme.exceptions;

public class UserNotFoundException extends DTUPayException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
