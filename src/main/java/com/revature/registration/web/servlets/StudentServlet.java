package com.revature.registration.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.registration.models.Student;
import com.revature.registration.services.UserServices;
import com.revature.registration.util.exceptions.DataSourceException;
import com.revature.registration.util.exceptions.InvalidInformationException;
import com.revature.registration.web.dtos.ErrorResponse;
import com.revature.registration.web.dtos.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class StudentServlet extends HttpServlet {

    private final UserServices userServices;
    private final ObjectMapper objectMapper;

    public StudentServlet(UserServices userServices, ObjectMapper objectMapper) {
        this.userServices = userServices;
        this.objectMapper = objectMapper;
    }

    // TODO fix this, it's probably wrong
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        try {
            HttpSession session = req.getSession(false);
            Principal principal = (session == null) ? null : (Principal) session.getAttribute("auth-user");

            if (principal == null) {
                String msg = "No session found, please login.";
                resp.setStatus(401);
                ErrorResponse errResp = new ErrorResponse(401, msg);
                respWriter.write(objectMapper.writeValueAsString(errResp));
                return;
            } else {
                System.out.println(principal);
                Student student = userServices.findStudentById(principal.getId());
                String payload = objectMapper.writeValueAsString(student);
                respWriter.write(payload);
            }
        } catch (InvalidInformationException iie) {
            iie.printStackTrace();
            resp.setStatus(401);
            ErrorResponse errorResponse = new ErrorResponse(401,iie.getMessage());
            respWriter.write(objectMapper.writeValueAsString(errorResponse));
        } catch (DataSourceException dse) {
            resp.setStatus(500);
            ErrorResponse errorResponse = new ErrorResponse(500,dse.getMessage());
            respWriter.write(objectMapper.writeValueAsString(errorResponse));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            ErrorResponse errorResponse = new ErrorResponse(500,"an unexpected error occurred");
            respWriter.write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}
