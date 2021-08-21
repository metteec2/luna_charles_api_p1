package com.revature.registration.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.registration.models.Student;
import com.revature.registration.services.CourseServices;
import com.revature.registration.services.UserServices;
import com.revature.registration.web.dtos.CourseEditDTO;
import com.revature.registration.web.dtos.ErrorResponse;
import com.revature.registration.web.dtos.Principal;
import com.revature.registration.web.dtos.RegistrationDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class RegistrationServlet extends HttpServlet {

    private final UserServices userServices;
    private final CourseServices courseServices;
    private final ObjectMapper objectMapper;

    public RegistrationServlet(UserServices userServices, CourseServices courseServices, ObjectMapper objectMapper) {
        this.userServices = userServices;
        this.courseServices = courseServices;
        this.objectMapper = objectMapper;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getMethod() + " received from client.");
        resp.getWriter().write("<h1>Register for Courses</h1>");
    }

    //for registering student to a course
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        try {
            HttpSession session = req.getSession(false);
            Principal principal = (session == null) ? null : (Principal) session.getAttribute("auth-user");

            if(principal == null){
                String msg = "No session found, please login.";
                resp.setStatus(401);
                ErrorResponse errResp = new ErrorResponse(401, msg);
                respWriter.write(objectMapper.writeValueAsString(errResp));
                return;
            } else {
                RegistrationDTO registrationDTO = objectMapper.readValue(req.getInputStream(), RegistrationDTO.class);

                if(registrationDTO.getAction().equals("Register")){
                    boolean approved = courseServices.registerForCourse(registrationDTO.getCourseNumber(), principal.getEmail());
                    if(approved){
                        String payload = objectMapper.writeValueAsString(approved);
                        respWriter.write(payload);
                        resp.setStatus(200);
                    } else {
                        resp.setStatus(404);
                        ErrorResponse errorResponse = new ErrorResponse(404, "Resource does not exist");
                        respWriter.write(objectMapper.writeValueAsString(errorResponse));
                        return;
                    }
                }
                if(registrationDTO.getAction().equals("Unregister")){
                    boolean approved = courseServices.removeFromCourse(registrationDTO.getCourseNumber(), principal.getEmail());
                    if(approved){
                        String payload = objectMapper.writeValueAsString(approved);
                        respWriter.write(payload);
                        resp.setStatus(200);
                    } else {
                        resp.setStatus(404);
                        ErrorResponse errorResponse = new ErrorResponse(404, "Resource does not exist");
                        respWriter.write(objectMapper.writeValueAsString(errorResponse));
                        return;
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}
