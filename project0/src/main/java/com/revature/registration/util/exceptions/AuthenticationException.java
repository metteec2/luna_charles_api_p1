package com.revature.registration.util.exceptions;

/**
 * Authentication Exceptions are thrown when a problem occurs authenticating a user. For example, if they provided an
 * incorrect password.
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException (String message) {
        super(message);
    }
}
