package com.revature.registration.util.exceptions;

/**
 * Invalid Information Exceptions are thrown when a user provides invalid information when attempting to register as
 * a new student or a faculty adding a course.
 */
public class InvalidInformationException extends RuntimeException {

    public InvalidInformationException(String message) {
        super(message);
    }
}
