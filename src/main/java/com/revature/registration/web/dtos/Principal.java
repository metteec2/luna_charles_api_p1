package com.revature.registration.web.dtos;

import com.revature.registration.models.Faculty;
import com.revature.registration.models.Student;

import java.util.Objects;

public class Principal {

    private String id;
    private String email;

    public Principal() {}

    public Principal(Student student) {
        this.id = student.getId();
        this.email = student.getEmail();
    }

    public Principal(Faculty faculty) {
        this.id = faculty.getId();
        this.email = getEmail();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Principal principal = (Principal) o;
        return Objects.equals(id, principal.id) && Objects.equals(email, principal.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "Principal{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}