package com.revature.registration.util.exceptions;

/**
 * Data Source Exceptions are thrown when problems specific to the database, or interacting with it, occur.
 */
public class DataSourceException extends RuntimeException{

    public DataSourceException (String message, Throwable cause) {
        super(message, cause);
    }
}
