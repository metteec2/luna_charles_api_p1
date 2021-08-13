package com.revature.registration.screens;

import com.revature.registration.util.AppState;
import com.revature.registration.util.ScreenRouter;
import java.io.BufferedReader;

/**
 * WelcomeScreen is the first page that the app displays and a de-facto home page. When registration or login fails,
 * users are sent back to the WelcomeScreen. From here, users can register, login, or exit the application.
 */
public class WelcomeScreen extends Screen {

    public WelcomeScreen(BufferedReader consoleReader, ScreenRouter router) {
        super("Welcome Screen", "/welcome", consoleReader, router);
    }

    /**
     * render() displays the options for this screen, including navigating to LoginScreen, navigating to
     * RegistrationScreen, and exiting the application.
     * @throws Exception
     */
    @Override
    public void render() throws Exception {
        System.out.println("----------------------------");
        System.out.println(
                "Course Management:\n" +
                "1) Login\n" +
                "2) Register New Student\n" +
                "3) Exit Application");
        System.out.print("> ");

        String userSelection = consoleReader.readLine();

        switch (userSelection) {
            // login
            case "1":
                router.navigate("/login");
                break;
            // register
            case "2":
                router.navigate("/registration");
                break;
            // exit
            case "3":
                System.out.println("Exiting Application...");
                AppState.shutdown();
                break;
            default:
                System.out.println("Please enter a valid input");
        }

    }
}
