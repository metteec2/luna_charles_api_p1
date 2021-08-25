package com.revature.registration.web.dtos;

import java.util.Objects;

public class RegistrationDTO {

    private String action;
    private String courseNumber;

    public RegistrationDTO(){}

    public RegistrationDTO(String action, String courseNumber) {
        this.action = action;
        this.courseNumber = courseNumber;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationDTO that = (RegistrationDTO) o;
        return Objects.equals(action, that.action) && Objects.equals(courseNumber, that.courseNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, courseNumber);
    }

    @Override
    public String toString() {
        return "RegistrationDTO{" +
                "action='" + action + '\'' +
                ", courseNumber='" + courseNumber + '\'' +
                '}';
    }
}
