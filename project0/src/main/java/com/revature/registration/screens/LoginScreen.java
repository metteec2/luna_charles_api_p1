package com.revature.registration.screens;

import com.revature.registration.models.Faculty;
import com.revature.registration.models.Student;
import com.revature.registration.services.UserServices;
import com.revature.registration.util.ScreenRouter;
import com.revature.registration.util.Session;
import com.revature.registration.util.exceptions.AuthenticationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;

/**
 * The LoginScreen uses UserServices to authenticate Faculty and Student users and navigates them to their
 * respective dashboards.
 */
public class LoginScreen extends Screen {

    private final Logger logger = LogManager.getLogger(LoginScreen.class);
    private final UserServices userServices;

    public LoginScreen(BufferedReader consoleReader, ScreenRouter router, UserServices userServices) {
        super("Login Screen", "/login", consoleReader, router);
        this.userServices = userServices;
    }

    /**
     * render includes method calls for authenticating users. It navigates them to the correct dashboard if they are
     * able to be authenticated. If for any reason a user cannot be authenticated, then they are sent back to the
     * welcome screen.
     * @throws Exception
     */
    @Override
    public void render() throws Exception {
        System.out.println("----------------------------");
        System.out.println("Login Screen:\n" +
                "1) Faculty\n" +
                "2) Student");
        System.out.print("> ");
        int userType = Integer.parseInt(consoleReader.readLine());

        System.out.println("Email: ");
        String email = consoleReader.readLine();

        System.out.println("Password: ");
        String password = consoleReader.readLine();
        try {
            switch (userType) {
                // faculty
                case 1:
                    Faculty faculty = userServices.loginFaculty(email, password);
                    Session.getInstance().setFaculty(faculty);
                    logger.info("Successfully logged in");
                    router.navigate("/facultydashboard");
                    break;
                // student
                case 2:
                    Student student = userServices.loginStudent(email, password);
                    Session.getInstance().setStudent(student);
                    logger.info("Successfully logged in");
                    router.navigate("/studentdashboard");
                    break;
                default:
                    System.out.println("Please enter a valid input");
            }
        } catch (AuthenticationException ae) {
            logger.error(ae.getMessage());
            logger.debug("User could not be authenticated");
            router.navigate("/welcome");
        }
    }
}
