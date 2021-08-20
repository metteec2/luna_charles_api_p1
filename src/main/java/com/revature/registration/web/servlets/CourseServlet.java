package com.revature.registration.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.registration.models.Course;
import com.revature.registration.services.CourseServices;
import com.revature.registration.services.UserServices;
import com.revature.registration.util.exceptions.DataSourceException;
import com.revature.registration.util.exceptions.InvalidInformationException;
import com.revature.registration.web.dtos.CourseDTO;
import com.revature.registration.web.dtos.ErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CourseServlet extends HttpServlet {

    private final CourseServices courseServices;
    private final UserServices userServices;
    private final ObjectMapper objectMapper;

    public CourseServlet(CourseServices courseServices, UserServices userServices, ObjectMapper objectMapper) {
        this.courseServices = courseServices;
        this.userServices = userServices;
        this.objectMapper = objectMapper;
    }

    //for getting courses that a faculty member teaches
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //#TODO logic for getting courses that a faculty member teaches
        PrintWriter printWriter = resp.getWriter();
        resp.setContentType("application/json");


        try{
            userServices.findFacultyById

        }

        /*
              need a way to access FacultyRepository to find the Faculty object
              (to send as a parameter into the .findByFaculty method in CourseRepository)
              but we can only access FacultyRepo through UserServices, not through
              CourseServices.
         */

    }

    //For adding a course
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();
        resp.setContentType("application/json");

        try {
            Course newCourse = objectMapper.readValue(req.getInputStream(), Course.class);
            CourseDTO courseDTO = new CourseDTO(courseServices.createCourse(newCourse));
            String payload = objectMapper.writeValueAsString(courseDTO);
            printWriter.write(payload);
            resp.setStatus(201);
        } catch (InvalidInformationException | MismatchedInputException e) {
            resp.setStatus(400);
            ErrorResponse errorResponse = new ErrorResponse(400, e.getMessage());
            printWriter.write(objectMapper.writeValueAsString(errorResponse));
        } catch (DataSourceException dse) {
            resp.setStatus(409);
            ErrorResponse errorResponse = new ErrorResponse(409, dse.getMessage());
            printWriter.write(objectMapper.writeValueAsString(errorResponse));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //#TODO logic for updating course info

        PrintWriter printWriter = resp.getWriter();
        resp.setContentType("application/json");

        /*
                to update course, we need
                    1. String currentNumber
                    2. String field
                    3. String newValue
                        How to get all three in a single input stream?
         */
    }

    //for deleting a course
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter printWriter = resp.getWriter();
        resp.setContentType("application/json");

        try {
            String courseNumber = objectMapper.readValue(req.getInputStream(), String.class);
            boolean accepted = courseServices.removeCourse(courseNumber);
            if(!accepted){
                resp.setStatus(404);
                ErrorResponse errorResponse = new ErrorResponse(404, "Resource does not exist");
                printWriter.write(objectMapper.writeValueAsString(errorResponse));
                return;
            } else {
                String payload = objectMapper.writeValueAsString(accepted);
                printWriter.write(payload);
                resp.setStatus(204);
            }
        } catch (Exception e){
            e.printStackTrace();
            resp.setStatus(500);
        }

    }

}
