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

public class AuthFacultyServlet extends HttpServlet {

    private final UserServices userServices;
    private final ObjectMapper objectMapper;

    public AuthFacultyServlet(UserServices userServices, ObjectMapper objectMapper) {
        this.userServices = userServices;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        try{
            Credentials credentials = objectMapper.readValue(req.getInputStream(), Credentials.class);
            Principal principal = new Principal(userServices.loginFaculty(credentials.getEmail(), credentials.getPassword()));

            String payload = objectMapper.writeValueAsString(principal);
            respWriter.write(payload);

            HttpSession session = req.getSession();
            session.setAttribute("auth-user",principal);

        } catch (AuthenticationException ae){
            resp.setStatus(401);
            ErrorResponse errorResponse = new ErrorResponse(401, ae.getMessage());
            respWriter.write(objectMapper.writeValueAsString(errorResponse));
        } catch (Exception e){
            e.printStackTrace();
            resp.setStatus(500);
            ErrorResponse errorResponse = new ErrorResponse(500, "an unexpected error occurred");
            respWriter.write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}
