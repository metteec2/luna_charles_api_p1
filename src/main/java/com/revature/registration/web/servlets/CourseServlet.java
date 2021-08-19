package com.revature.registration.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.registration.models.Course;
import com.revature.registration.services.CourseServices;
import com.revature.registration.services.UserServices;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CourseServlet extends HttpServlet {

    private final CourseServices courseServices;
    private final ObjectMapper objectMapper;

    public CourseServlet(CourseServices courseServices, ObjectMapper objectMapper) {
        this.courseServices = courseServices;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getMethod() + " received from client.");
        resp.getWriter().write("<h1>Adjust Courses</h1>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //#TODO logic for adding courses

        PrintWriter printWriter = resp.getWriter();
        resp.setContentType("application/json");

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //#TODO logic for updating course info
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //#TODO logic for deleting a course
    }
}
