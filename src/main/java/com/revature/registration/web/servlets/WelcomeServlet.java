package com.revature.registration.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WelcomeServlet extends HttpServlet {

    private final ObjectMapper objectMapper;

    public WelcomeServlet(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getMethod() + " received from client.");
        resp.getWriter().write("<h1>course registration!</h1>");
    }

    // TODO after hearing today's lesson, this logic appears to be something we'll handle in the ui
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // not sure if this will be a parameter or attribute, but either way it shouldn't be difficult to access
        String choice = req.getParameter("choice");

        switch (choice) {
            case "Student Login":
                // use a dispatcher to forward the request to StudentAuth
                // StudentAuth will either throw an AuthenticationException or redirect them to another page
                break;
            case "Faculty Login":
                // same as student, but with faculty things
                break;
            case "Register New Student":
                // use a dispatcher to forward the request to StudentServlet
                // StudentServlet will register the student, throw an exception if they don't input valid information
                // or log them in and redirect them to /student if it succeeds.
                break;
            default:
                // throw an exception here, not sure how they got here
        }
    }
}
