package com.revature.registration.util;

import com.revature.registration.screens.Screen;
import java.util.HashSet;
import java.util.Set;

/**
 * ScreenRouter handles the changing between Screens, using Screen's routes to navigate between them on command.
 */
public class ScreenRouter {

    private Screen currentScreen;
    private Set<Screen> screens = new HashSet<>();

    /**
     * addScreen inserts a screen into the Set of Screens that ScreenRouter holds. This is used at app startup to load
     * the Screens so that they can be displayed easily.
     * @param screen
     * @return
     */
    public ScreenRouter addScreen(Screen screen) {
        screens.add(screen);
        return this;
    }

    /**
     * navigate() sets the current screen to a Screen specified by a particular route. AppState's strartup() will
     * change the screen the next time it gets to the top of its while loop.
     * @param route
     */
    public void navigate(String route) {
        for (Screen screen : screens) {
            if (screen.getRoute().equals(route)) {
                currentScreen = screen;
                break;
            }
        }
    }

    public Screen getCurrentScreen() { return currentScreen; }
}
