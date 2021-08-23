package com.revature.registration.web.dtos;

import java.util.Objects;

public class CourseEditDTO {

    private String currentNumber;
    private String field;
    private String newValue;

    public CourseEditDTO(){}

    public CourseEditDTO(String currentNumber, String field, String newValue) {
        this.currentNumber = currentNumber;
        this.field = field;
        this.newValue = newValue;
    }

    public String getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(String currentNumber) {
        this.currentNumber = currentNumber;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseEditDTO that = (CourseEditDTO) o;
        return Objects.equals(currentNumber, that.currentNumber) && Objects.equals(field, that.field) && Objects.equals(newValue, that.newValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentNumber, field, newValue);
    }

    @Override
    public String toString() {
        return "CourseEditDTO{" +
                "currentNumber='" + currentNumber + '\'' +
                ", field='" + field + '\'' +
                ", newValue='" + newValue + '\'' +
                '}';
    }
}
