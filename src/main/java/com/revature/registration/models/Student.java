package com.revature.registration.models;

/**
 * The Student Class is similar, if  not identical, to the Faculty Class. They exist separately as users to make code
 * more legible and understandable; Students and Faculty are stored separately in the database for permissions reasons
 */
public class Student extends User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String id;

    public Student() {}
}
