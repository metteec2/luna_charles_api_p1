package com.revature.registration.screens;

import com.revature.registration.models.Course;
import com.revature.registration.models.Faculty;
import com.revature.registration.services.CourseServices;
import com.revature.registration.services.UserServices;
import com.revature.registration.util.AppState;
import com.revature.registration.util.ScreenRouter;
import com.revature.registration.util.Session;
import com.revature.registration.util.exceptions.InvalidInformationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;

/**
 * the FacultyDashboard is where Faculty the have successfully logged in perform CRUD operations on Courses they teach.
 */
public class FacultyDashboard extends Screen {

    private final Logger logger = LogManager.getLogger(FacultyDashboard.class);
    private final UserServices userServices;
    private final CourseServices courseServices;
    public Faculty faculty;

    public FacultyDashboard(BufferedReader consoleReader, ScreenRouter router, UserServices userServices,
                            CourseServices courseServices) {
        super("Faculty Dashboard", "/facultydashboard", consoleReader, router);
        this.userServices = userServices;
        this.courseServices = courseServices;
    }

    /**
     * render displays the faculty dashboard. It includes options for adding, updating, and removing courses, along
     * with viewing user info and exiting the application. These options contains calls to other methods to
     * communicate with other parts of the application.
     * @throws Exception
     */
    @Override
    public void render() throws Exception {
        System.out.println("----------------------------");
        Faculty faculty = Session.getInstance().getFaculty();
        System.out.println( "Faculty Dashboard\n" +
                            "1) Add a New Course\n" +
                            "2) Update Course Registration Details\n" +
                            "3) Remove a Course\n" +
                            "4) View User Info\n" +
                            "5) Exit Application");
        System.out.print("> ");
        String userChoice = consoleReader.readLine();

        switch (Integer.parseInt(userChoice)) {
            // add course
            case 1:
                System.out.println("Add a Course");
                System.out.println("Enter Course Number");
                System.out.print("> ");
                String number = consoleReader.readLine();

                System.out.println("Enter Course Name");
                System.out.print("> ");
                String name = consoleReader.readLine();

                System.out.println("Enter Course Description");
                System.out.print("> ");
                String description = consoleReader.readLine();

                System.out.println("Enter Course Capacity");
                System.out.print("> ");
                int capacity = Integer.parseInt(consoleReader.readLine());
                try {
                    Course addCourse = new Course();
                    addCourse.setNumber(number);
                    addCourse.setName(name);
                    addCourse.setDescription(description);
                    addCourse.setProfessor(faculty.getEmail());
                    addCourse.setCapacity(capacity);
                    courseServices.createCourse(addCourse);
                    logger.info("Course successfully added");
                } catch (InvalidInformationException iie) {
                    logger.error(iie.getMessage());
                    logger.debug("Course not added");
                }
                break;
            // update a course
            case 2:
                System.out.println("Update a Course");

                System.out.println("Enter current course number");
                System.out.print("> ");
                String currentNumber = consoleReader.readLine();

                System.out.println("Enter field to update:\n" +
                        "(number, name, or description)");
                System.out.print("> ");
                String field = consoleReader.readLine();

                System.out.println("Enter new value for that field:");
                System.out.print("> ");
                String newValue = consoleReader.readLine();

                if (!courseServices.updateCourse(currentNumber,field,newValue)) {
                    System.out.println("could not update course");
                    break;
                }
                courseServices.updateCourse(currentNumber,field,newValue);
                break;
            // delete course
            case 3:
                System.out.println("Remove a Course");
                System.out.println("Enter Course Number:");
                System.out.print("> ");
                String removeCourseNumber = consoleReader.readLine();

                courseServices.removeCourse(removeCourseNumber);
                break;
            // view user info, including courses taught
            case 4:
                System.out.println("User Info:");
                System.out.println(faculty.getFirstName());
                System.out.println(faculty.getLastName());
                System.out.println(faculty.getEmail());
                System.out.println("Courses Taught:");
                for (Course c : courseServices.getTaughtCourses(faculty)) {
                    System.out.println("displaying course:");
                    System.out.println(c.toString());
                }
                break;
            // exit
            case 5:
                System.out.println("Exiting Application");
                AppState.shutdown();
                break;
            default:
                System.out.println("Please enter a valid input");
        }
    }
}
