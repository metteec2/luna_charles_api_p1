package com.revature.registration.models;

/**
 * The Faculty class is similar, if not identical, to the Student class. They exist separately as users to
 * make code more legible and understandable; students and faculty are stored separated in the database for
 * permissions reasons.
 */
public class Faculty extends User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String id;

    public Faculty() {}
}
