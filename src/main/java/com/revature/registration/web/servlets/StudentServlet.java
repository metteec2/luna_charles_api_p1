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
import java.util.ArrayList;
import java.util.List;

public class StudentServlet extends HttpServlet {

    private final UserServices userServices;
    private final CourseServices courseServices;
    private final ObjectMapper objectMapper;

    public StudentServlet(UserServices userServices, CourseServices courseServices, ObjectMapper objectMapper) {
        this.userServices = userServices;
        this.objectMapper = objectMapper;
        this.courseServices = courseServices;
    }

    /**
     * Method for getting a student's information. The response is an array of objects. The first (index 0) object includes
     *   the first name, last name, and email of the requesting student. The other elements in the array are courses, which
     *   have number, name, description, professor, and students[] fields.
     * @param req
     * @param resp - will contain an array with the authorized student's information
     * @throws ServletException
     * @throws IOException
     */
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
                Student student = userServices.findStudentById(principal.getId());
                StudentDTO studentDTO = new StudentDTO(student);
                List<Object> queryResult = new ArrayList<>();
                queryResult.add(studentDTO);
                for (Course c : courseServices.getRegisteredCourses(student)) {
                    CourseDTO dto = new CourseDTO(c);
                    queryResult.add(dto);
                }
                String payload = objectMapper.writeValueAsString(queryResult);
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

    /**
     * Method for registering a new student.
     * @param req - JSON with firstName, lastName, email, and password fields
     * @param resp - will contain the newly registered user's ID, email, and role (student).
     * @throws ServletException
     * @throws IOException
     */
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
