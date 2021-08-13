package com.revature.registration;

import com.revature.registration.util.AppState;

/**
 * App contains the main() method where the app will be running, which contains an AppState that is started.
 */
public class App {
    public static void main(String[] args) {
        AppState app = new AppState();
        app.startup();
    }
}
