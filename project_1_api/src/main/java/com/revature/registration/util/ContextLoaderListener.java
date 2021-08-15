package com.revature.registration.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.revature.registration.repositories.CourseRepository;
import com.revature.registration.repositories.FacultyRepository;
import com.revature.registration.repositories.StudentRepository;
import com.revature.registration.services.CourseServices;
import com.revature.registration.services.UserServices;
import com.revature.registration.web.servlets.WelcomeServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
        ObjectMapper objectMapper = new ObjectMapper();

        CourseRepository courseRepository = new CourseRepository();
        FacultyRepository facultyRepository = new FacultyRepository();
        StudentRepository studentRepository = new StudentRepository();

        CourseServices courseServices = new CourseServices(courseRepository);
        UserServices userServices = new UserServices(studentRepository,facultyRepository);

        WelcomeServlet welcomeServlet = new WelcomeServlet(objectMapper);

        ServletContext servletContext = sce.getServletContext();
        servletContext.addServlet("WelcomeServlet",welcomeServlet).addMapping("/welcome");
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionFactory.getInstance().cleanUp();
    }
}
