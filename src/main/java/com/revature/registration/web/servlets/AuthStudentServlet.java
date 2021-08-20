package com.revature.registration.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.registration.services.UserServices;
import com.revature.registration.util.exceptions.AuthenticationException;
import com.revature.registration.web.dtos.Credentials;
import com.revature.registration.web.dtos.ErrorResponse;
import com.revature.registration.web.dtos.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthStudentServlet extends HttpServlet {

    private final UserServices userServices;
    private final ObjectMapper objectMapper;

    public AuthStudentServlet(UserServices userServices,ObjectMapper objectMapper) {
        this.userServices = userServices;
        this.objectMapper = objectMapper;
    }

    // the endpoint that this servlet is mapped to will be hit by a Post request, triggering this method
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // the response writer will send data it gets from the service layer, or exceptions, as json to the ui
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        try {
            // use the object mapper to read the inputs given by the user as Credentials
            Credentials credentials = objectMapper.readValue(req.getInputStream(), Credentials.class);
            // send those credentials to the user services to log them in, may throw an authentication exception
            Principal principal = new Principal(userServices.loginStudent(credentials.getEmail(),credentials.getPassword()));
            // map the new object, the principal, back into json and send it to the ui
            String payload = objectMapper.writeValueAsString(principal);
            respWriter.write(payload);

            // the user's session will stay with them over time, meaning we can reference it when needed.
            // in this case, we add an "auth-user" attribute so that we can ensure a user is meant to be on a webpage later on
            HttpSession session = req.getSession();
            session.setAttribute("auth-user",principal);

        // loginStudent throws and Authentication Exception if its unable to log a user in. This sets the status code
        // to 401 sends that back, along with the exception's message
        } catch (AuthenticationException ae) {
            resp.setStatus(401);
            ErrorResponse errorResponse = new ErrorResponse(401,ae.getMessage());
            respWriter.write(objectMapper.writeValueAsString(errorResponse));
        // if any other exceptions occur that we don't know about, then print the stacktrace and send back
        // a status code of 500, which basically means "it's not you, it's me"
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            ErrorResponse errorResponse = new ErrorResponse(500,"an unexpected error occurred");
            respWriter.write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}
