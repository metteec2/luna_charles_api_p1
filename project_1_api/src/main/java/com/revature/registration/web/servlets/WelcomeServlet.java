package com.revature.registration.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.registration.services.UserServices;

import javax.servlet.http.HttpServlet;

public class WelcomeServlet extends HttpServlet {

    private final ObjectMapper objectMapper;

    public WelcomeServlet(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
