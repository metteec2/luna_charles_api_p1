package com.revature.registration.screens;

import com.revature.registration.models.Student;
import com.revature.registration.services.UserServices;
import com.revature.registration.util.ScreenRouter;
import com.revature.registration.util.exceptions.InvalidInformationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;

/**
 * The RegistrationScreen allows users to register as a new student in the system and have their data persisted.
 */
public class RegistrationScreen extends Screen {

    private final Logger logger = LogManager.getLogger(RegistrationScreen.class);
    private final UserServices userServices;

    public RegistrationScreen(BufferedReader consoleReader, ScreenRouter router, UserServices userServices) {
        super("Registration Screen", "/registration", consoleReader, router);
        this.userServices = userServices;
    }

    /**
     * When render() is called it gathers user information then attempts to register the user. render() then handles
     * success and failure before sending the user back to the welcome screen.
     * @throws Exception
     */
    @Override
    public void render() throws Exception {
        System.out.println("----------------------------");
        System.out.println("Register a new student account:");

        System.out.println("First Name: ");
        String firstName = consoleReader.readLine();

        System.out.println("Last Name: ");
        String lastName = consoleReader.readLine();

        System.out.println("Email: ");
        String email = consoleReader.readLine();

        System.out.println("Password: ");
        String password = consoleReader.readLine();

        Student newStudent = new Student();
        newStudent.setFirstName(firstName);
        newStudent.setLastName(lastName);
        newStudent.setEmail(email);
        newStudent.setPassword(password);
        try {
            userServices.registerStudent(newStudent);
            logger.info("New student registered");
            router.navigate("/welcome");
        } catch (InvalidInformationException iie) {
            logger.error(iie.getMessage());
            logger.debug("Student was not registered");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            router.navigate("/welcome");
        }
    }
}
