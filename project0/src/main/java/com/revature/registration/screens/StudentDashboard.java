package com.revature.registration.screens;

import com.revature.registration.models.Course;
import com.revature.registration.models.Student;
import com.revature.registration.services.CourseServices;
import com.revature.registration.services.UserServices;
import com.revature.registration.util.AppState;
import com.revature.registration.util.ScreenRouter;
import com.revature.registration.util.Session;
import java.io.BufferedReader;

/**
 * StudentDashboard contains options for Students to perform CRUD operations involving Courses they are, or might
 * want to be, registered for.
 */
public class StudentDashboard extends Screen {

    private final UserServices userServices;
    private final CourseServices courseServices;
    public Student student;

    public StudentDashboard(BufferedReader consoleReader, ScreenRouter router, UserServices userServices,
                            CourseServices courseServices) {
        super("Student Dashboard", "/studentdashboard", consoleReader, router);
        this.userServices = userServices;
        this.courseServices = courseServices;
    }

    /**
     * render() gives Students options to view the entire course list, register for a course, view courses they are
     * registered for, cancel their registration for a course, view their user info, or exit. Each of these options
     * collects information from the user to accurately perform the appropriate operations.
     * @throws Exception
     */
    @Override
    public void render() throws Exception {
        System.out.println("----------------------------");
        Student student = Session.getInstance().getStudent();
        System.out.println( "Student Dashboard\n" +
                            "1) View Course List\n" +
                            "2) Register for a Course\n" +
                            "3) View Your Registered Courses\n" +
                            "4) Cancel Your Registration\n" +
                            "5) View User Info\n" +
                            "6) Exit Application");
        System.out.print("> ");
        String userChoice = consoleReader.readLine();


        switch (Integer.parseInt(userChoice)) {
            // view entire course list
            case 1:
                System.out.println("Course List:");
                for (Course c : courseServices.getCourseList()) {
                    System.out.println(c.toString());
                }
                break;
            // register for a course
            case 2:
                System.out.println("Enter Course Number: ");
                String courseRegNumber = consoleReader.readLine();
                courseServices.registerForCourse(courseRegNumber,student);
                break;
            // view registered courses
            case 3:
                System.out.println("Your Registered Courses:");
                for ( Course c : courseServices.getRegisteredCourses(student)) {
                    System.out.println(c.toString());
                }
                break;
            // unregister
            case 4:
                System.out.println("Enter Course Number:");
                String courseDelNumber = consoleReader.readLine();
                courseServices.removeFromCourse(courseDelNumber,student);
                break;
            // view user info
            case 5:
                System.out.println("User Info:");
                System.out.println(student.getFirstName());
                System.out.println(student.getLastName());
                System.out.println(student.getEmail());
                break;
            // exit
            case 6:
                System.out.println("Exiting Application...");
                AppState.shutdown();
                break;
            default:
                System.out.println("Please enter a valid input");
        }
    }
}
