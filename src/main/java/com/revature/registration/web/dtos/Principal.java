package com.revature.registration.web.dtos;

import com.revature.registration.models.Faculty;
import com.revature.registration.models.Student;
import io.jsonwebtoken.Claims;

import java.util.Objects;

public class Principal {

    private String id;
    private String email;
    private String role;

    public Principal() {}

    public Principal(Claims jwtClaims) {
        this.id = jwtClaims.getId();
        this.email = jwtClaims.getSubject();
        this.role = jwtClaims.get("role", String.class);
    }

    public Principal(Student student) {
        this.id = student.getId();
        this.email = student.getEmail();
        this.role = "student";
    }

    public Principal(Faculty faculty) {
        this.id = faculty.getId();
        this.email = faculty.getEmail();
        this.role = "faculty";
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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