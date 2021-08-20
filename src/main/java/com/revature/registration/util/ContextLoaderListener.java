package com.revature.registration.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.revature.registration.repositories.CourseRepository;
import com.revature.registration.repositories.FacultyRepository;
import com.revature.registration.repositories.StudentRepository;
import com.revature.registration.services.CourseServices;
import com.revature.registration.services.UserServices;
import com.revature.registration.web.servlets.*;

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
        HealthCheckServlet healthCheckServlet = new HealthCheckServlet();
        CourseServlet courseServlet = new CourseServlet();
        RegistrationServlet registrationServlet = new RegistrationServlet();
        StudentServlet studentServlet = new StudentServlet();
        FacultyServlet facultyServlet = new FacultyServlet();
        AuthStudentServlet authStudentServlet = new AuthStudentServlet(userServices,objectMapper);
        AuthFacultyServlet authFacultyServlet = new AuthFacultyServlet(userServices,objectMapper);

        ServletContext servletContext = sce.getServletContext();
        servletContext.addServlet("WelcomeServlet",welcomeServlet).addMapping("/welcome");
        servletContext.addServlet("HealthCheckServlet",healthCheckServlet).addMapping("/health");
        servletContext.addServlet("CourseServlet",courseServlet).addMapping("/course");
        servletContext.addServlet("RegistrationServlet",registrationServlet).addMapping("/registration");
        servletContext.addServlet("StudentServlet",studentServlet).addMapping("/student");
        servletContext.addServlet("FacultyServlet",facultyServlet).addMapping("/faculty");
        servletContext.addServlet("AuthStudentServlet",authStudentServlet).addMapping("/authstudent");
        servletContext.addServlet("AuthFacultyServlet",authFacultyServlet).addMapping("/authfaculty");
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionFactory.getInstance().cleanUp();
    }
}
