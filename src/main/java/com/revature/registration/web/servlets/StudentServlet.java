package com.revature.registration.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.registration.models.Course;
import com.revature.registration.models.Student;
import com.revature.registration.services.CourseServices;
import com.revature.registration.services.UserServices;
import com.revature.registration.util.exceptions.DataSourceException;
import com.revature.registration.util.exceptions.InvalidInformationException;
import com.revature.registration.web.dtos.CourseDTO;
import com.revature.registration.web.dtos.ErrorResponse;
import com.revature.registration.web.dtos.Principal;
import com.revature.registration.web.dtos.StudentDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class StudentServlet extends HttpServlet {

    private final UserServices userServices;
    private final CourseServices courseServices;
    private final ObjectMapper objectMapper;

    public StudentServlet(UserServices userServices, CourseServices courseServices, ObjectMapper objectMapper) {
        this.userServices = userServices;
        this.objectMapper = objectMapper;
        this.courseServices = courseServices;
    }

    // TODO add query param "course". if we have that param, then give list of registered courses. else, give student info.
    // view student information
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");



        try {
            Principal principal = (Principal) req.getAttribute("principal");

            if (principal == null) {
                String msg = "No session found, please login.";
                resp.setStatus(401);
                ErrorResponse errResp = new ErrorResponse(401, msg);
                respWriter.write(objectMapper.writeValueAsString(errResp));
                return;
            } else {
                System.out.println(principal);
                // TODO overload getRegisteredCourses so that it takes in a student email, or maybe id
                Student student = userServices.findStudentById(principal.getId());
                StudentDTO studentDTO = new StudentDTO(student);
                String payload = objectMapper.writeValueAsString(studentDTO);
                for (Course c : courseServices.getRegisteredCourses(student)) {
                    CourseDTO dto = new CourseDTO(c);
                    payload += ",\n";
                    payload += objectMapper.writeValueAsString(dto);
                }
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

    //For student registration
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter printWriter = resp.getWriter();
        resp.setContentType("application/json");

        try {
            Student newStudent = objectMapper.readValue(req.getInputStream(), Student.class);
            Principal principal = new Principal(userServices.registerStudent(newStudent));
            String payload = objectMapper.writeValueAsString(principal);
            printWriter.write(payload);
            resp.setStatus(201);
        } catch (InvalidInformationException | MismatchedInputException e) {
            resp.setStatus(400);
            ErrorResponse errorResponse = new ErrorResponse(400, e.getMessage());
            printWriter.write(objectMapper.writeValueAsString(errorResponse));
        } catch (DataSourceException dre) {
            resp.setStatus(409);
            ErrorResponse errorResponse = new ErrorResponse(409, dre.getMessage());
            printWriter.write(objectMapper.writeValueAsString(errorResponse));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}
