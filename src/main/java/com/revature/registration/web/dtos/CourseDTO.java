package com.revature.registration.web.dtos;

import com.revature.registration.models.Course;

import java.util.Arrays;
import java.util.Objects;

public class CourseDTO {

    private String number;
    private String name;
    private String description;
    private String professor;
    private int capacity;
    private String[] students;

    public CourseDTO(Course course){
        this.number = course.getNumber();
        this.name = course.getName();
        this.description = course.getDescription();
        this.professor = course.getProfessor();
        this.capacity = course.getCapacity();
        this.students = course.getStudents();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String[] getStudents() {
        return students;
    }

    public void setStudents(String[] students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseDTO courseDTO = (CourseDTO) o;
        return capacity == courseDTO.capacity && Objects.equals(number, courseDTO.number) && Objects.equals(name, courseDTO.name) && Objects.equals(description, courseDTO.description) && Objects.equals(professor, courseDTO.professor) && Arrays.equals(students, courseDTO.students);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(number, name, description, professor, capacity);
        result = 31 * result + Arrays.hashCode(students);
        return result;
    }

    @Override
    public String toString() {
        return "CourseDTO{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", professor='" + professor + '\'' +
                ", capacity=" + capacity +
                ", students=" + Arrays.toString(students) +
                '}';
    }
}
