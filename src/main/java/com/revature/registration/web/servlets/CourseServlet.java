package com.revature.registration.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.registration.models.Course;
import com.revature.registration.models.Faculty;
import com.revature.registration.services.CourseServices;
import com.revature.registration.services.UserServices;
import com.revature.registration.util.exceptions.DataSourceException;
import com.revature.registration.util.exceptions.InvalidInformationException;
import com.revature.registration.web.dtos.CourseDTO;
import com.revature.registration.web.dtos.CourseEditDTO;
import com.revature.registration.web.dtos.ErrorResponse;
import com.revature.registration.web.dtos.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CourseServlet extends HttpServlet {

    private final CourseServices courseServices;
    private final UserServices userServices;
    private final ObjectMapper objectMapper;

    public CourseServlet(CourseServices courseServices, UserServices userServices, ObjectMapper objectMapper) {
        this.courseServices = courseServices;
        this.userServices = userServices;
        this.objectMapper = objectMapper;
    }

    /**
     * Method for getting a list of courses that the faculty member currently authenticated teaches
     * Overrides HttpServlet class's doGet method.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        try {
            Principal principal = (Principal) req.getAttribute("principal");

            if(principal == null){
                String msg = ("No session found, please login.");
                resp.setStatus(401);
                ErrorResponse errResp = new ErrorResponse(401, msg);
                respWriter.write(objectMapper.writeValueAsString(errResp));
                return;
            } else {
                Faculty faculty = userServices.findFacultyById(principal.getId());
                List<Course> taughtCourses = courseServices.getTaughtCourses(faculty);
                String payload = objectMapper.writeValueAsString(taughtCourses);
                respWriter.write(payload);
                resp.setStatus(200);
            }
        } catch (InvalidInformationException iie) {
            iie.printStackTrace();
            resp.setStatus(401);
            ErrorResponse errorResponse = new ErrorResponse(401,iie.getMessage());
            respWriter.write(objectMapper.writeValueAsString(errorResponse));
        } catch (DataSourceException dse) {
            dse.printStackTrace();
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
     * Method for posting a new course to the database
     * Overrides HttpServlet class's doPost method
     * @param req - contains JSON with number, name, description, professor, capacity, and students fields
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        try {
            Principal principal = (Principal) req.getAttribute("principal");
            if(principal == null) {
                String msg = ("No session found, please login.");
                resp.setStatus(401);
                ErrorResponse errResp = new ErrorResponse(401, msg);
                respWriter.write(objectMapper.writeValueAsString(errResp));
                return;
            } else {
                Course newCourse = objectMapper.readValue(req.getInputStream(), Course.class);
                CourseDTO courseDTO = new CourseDTO(courseServices.createCourse(newCourse));
                String payload = objectMapper.writeValueAsString(courseDTO);
                respWriter.write(payload);
                resp.setStatus(201);
            }
        } catch (InvalidInformationException | MismatchedInputException e) {
            e.printStackTrace();
            resp.setStatus(400);
            ErrorResponse errorResponse = new ErrorResponse(400, e.getMessage());
            respWriter.write(objectMapper.writeValueAsString(errorResponse));
        } catch (DataSourceException dse) {
            dse.printStackTrace();
            resp.setStatus(409);
            ErrorResponse errorResponse = new ErrorResponse(409, dse.getMessage());
            respWriter.write(objectMapper.writeValueAsString(errorResponse));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            ErrorResponse errorResponse = new ErrorResponse(500,"an unexpected error occurred");
            respWriter.write(objectMapper.writeValueAsString(errorResponse));
        }
    }

    /**
     * Method for updating existing course
     * Overrides HttpServlet class's doPut method
     * @param req - has fields for current Number, field, and new Value
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        try {
            Principal principal = (Principal) req.getAttribute("principal");
            if (principal == null) {
                String msg = ("No session found, please login.");
                resp.setStatus(401);
                ErrorResponse errResp = new ErrorResponse(401, msg);
                respWriter.write(objectMapper.writeValueAsString(errResp));
                return;
            }

            CourseEditDTO courseEdit = objectMapper.readValue(req.getInputStream(), CourseEditDTO.class);
            boolean accepted = courseServices.updateCourse(courseEdit.getCurrentNumber(), courseEdit.getField(), courseEdit.getNewValue());
            if (accepted) {
                String payload = objectMapper.writeValueAsString(accepted);
                respWriter.write(payload);
                resp.setStatus(200); //accepted
            } else {
                resp.setStatus(404); //not found
                ErrorResponse errorResponse = new ErrorResponse(404, "Resource does not exist");
                respWriter.write(objectMapper.writeValueAsString(errorResponse));
                return;
            }
        } catch (InvalidInformationException | DataSourceException ee){
            ee.printStackTrace();
            resp.setStatus(400);
            ErrorResponse errorResponse = new ErrorResponse(400,ee.getMessage());
            respWriter.write(objectMapper.writeValueAsString(errorResponse));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            ErrorResponse errorResponse = new ErrorResponse(500,"an unexpected error occurred");
            respWriter.write(objectMapper.writeValueAsString(errorResponse));
        }
    }

    /**
     * Method for deleting an existing course
     * Overrides HttpServlet class's doDelete method
     * @param req - contains number of the course to be deleted
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        try {
            Principal principal = (Principal) req.getAttribute("principal");
            if(principal == null) {
                String msg = ("No session found, please login.");
                resp.setStatus(401);
                ErrorResponse errResp = new ErrorResponse(401, msg);
                respWriter.write(objectMapper.writeValueAsString(errResp));
                return;
            }

            String courseNumber = objectMapper.readValue(req.getInputStream(), String.class);
            boolean accepted = courseServices.removeCourse(courseNumber);
            if(!accepted){
                resp.setStatus(404);
                ErrorResponse errorResponse = new ErrorResponse(404, "Resource does not exist");
                respWriter.write(objectMapper.writeValueAsString(errorResponse));
                return;
            } else {
                String payload = objectMapper.writeValueAsString(accepted);
                respWriter.write(payload);
                resp.setStatus(204);
            }
        } catch (Exception e){
            e.printStackTrace();
            resp.setStatus(500);
            ErrorResponse errorResponse = new ErrorResponse(500,"an unexpected error occurred");
            respWriter.write(objectMapper.writeValueAsString(errorResponse));
        }

    }

}