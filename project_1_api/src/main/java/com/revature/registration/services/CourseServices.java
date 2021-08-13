package com.revature.registration.services;

import com.revature.registration.models.Course;
import com.revature.registration.models.Faculty;
import com.revature.registration.models.Student;
import com.revature.registration.repositories.CourseRepository;
import com.revature.registration.util.exceptions.DataSourceException;
import com.revature.registration.util.exceptions.InvalidInformationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

/**
 * CourseServices contains methods that A) take input from Screens, validates that input, and passes it to Repository
 * Classes for persistence and/or B) call a method from a Repository Class to pass into a Screen.
 */
public class CourseServices {

    // TODO implement slf4j (logback?)
    private final Logger logger = LogManager.getLogger(CourseServices.class);
    private CourseRepository courseRepo;

    public CourseServices(CourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    /**
     * createCourse() takes in a Course, validates it with isCourseValid, and returns the result of a call for the
     * course repository to save it.
     * @param newCourse
     * @return
     */
    public Course createCourse(Course newCourse) {
        isCourseValid(newCourse);
        return courseRepo.save(newCourse);
    }

    /**
     * getCourseList() calls findAll() from CourseRepository and returns the List of Courses it gets from that method
     * @return
     */
    public List<Course> getCourseList() {
        try {
            return courseRepo.findAll();
        } catch (DataSourceException dse) {
            logger.error(dse.getMessage());
        }
        return null;
    }

    /**
     * getRegisteredCourses() uses CourseRepository to search for courses with a particular student and returns a
     * List of those Courses.
     * @param student
     * @return
     */
    public List<Course> getRegisteredCourses(Student student) {
        try {
            return courseRepo.findByStudent(student);
        } catch (DataSourceException dse) {
            logger.error(dse.getMessage());
        }
        return null;
    }

    /**
     * getTaughtCourses() uses CourseRepository to search for courses with a particular professor and returns a List
     * of those Courses.
     * @param faculty
     * @return
     */
    public List<Course> getTaughtCourses(Faculty faculty) {
        try {
            return courseRepo.findByFaculty(faculty);
        } catch (DataSourceException dse) {
            logger.error(dse.getMessage());
        }
        return null;
    }

    /**
     * registerForCourse() uses CourseRepository to add a student to a Course's array of Students.
     * @param number
     * @param student
     */
    public void registerForCourse(String number, Student student) {
        try {
            courseRepo.addStudent(number, student.getEmail());
        } catch (Exception e) {
            logger.error("A problem occurred while trying to add student to course list, " +
                    "check that you aren't already registered");
            throw new DataSourceException(e.getMessage(),e);
        }
    }

    /**
     * removeFromCourse() uses CourseRepository to remove a Student from a Course's array of Students.
     * @param number
     * @param student
     */
    public  void removeFromCourse(String number, Student student) {
        courseRepo.removeStudent(number,student.getEmail());
    }

    /**
     * updateCourse() passes a course number, field, and value given to it into the CourseRepository Class to be
     * updated.
     * @param currentNumber
     * @param field
     * @param newValue
     * @return
     */
    public boolean updateCourse(String currentNumber,String field, String newValue) {
        return courseRepo.update(currentNumber,field,newValue);
    }

    /**
     * removeCourse takes in a course number and passes it to CourseRepository to delete the Course with that number.
     * @param number
     * @return
     */
    public boolean removeCourse(String number) {
        return courseRepo.deleteByNumber(number);
    }

    /**
     * isCourseValid contains the logic to check if a Course is valid before adding it to the database. It checks if
     * number or name are null or empty strings, if capacity is 0 or less, and if the description is too long.
     * @param course
     * @return
     * @throws InvalidInformationException
     */
    public boolean isCourseValid (Course course) throws InvalidInformationException {
        if (course.getCapacity() < 1) {
            throw new InvalidInformationException("Course capacity must be at least 1");
        }
        if (course.getDescription().length() > 280) {
            throw new InvalidInformationException("Course description cannot be more than 279 characters");
        }
        if (course.getNumber() == null || course.getName() == null ||
                course.getNumber().equals("") || course.getName().equals("")) {

            throw new InvalidInformationException("Course number/name cannot be null or empty");
        }
        return true;
    }

}
