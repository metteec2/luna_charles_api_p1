package com.revature.registration.screens;

import com.revature.registration.util.ScreenRouter;
import java.io.BufferedReader;

/**
 * This abstract class serves as a template for other screens, as well as a place for common methods. Every screen
 * extends this class to ensure that all of the Screens in this application have a name and a route, both with getters,
 * a BufferedReader, and a render() method.
 */
public abstract class Screen {

    protected String name;
    protected String route;
    protected BufferedReader consoleReader;
    protected ScreenRouter router;

    public Screen(String name, String route, BufferedReader consoleReader, ScreenRouter router) {
        this.name = name;
        this.route = route;
        this.consoleReader = consoleReader;
        this.router = router;
    }

    /**
     * getName returns the Screen's name (i.e. RegistrationScreen, WelcomeScreen)
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * getRoute returns the Screen's route (i.e. /registration, /welcome)
     * @return
     */
    public String getRoute() {
        return route;
    }

    /**
     * render() will display the menu for each screen, prompt users for input, and pass that input elsewhere to be
     * handled
     * @throws Exception
     */
    public abstract void render() throws Exception;

}
