package com.revature.registration.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.revature.registration.repositories.CourseRepository;
import com.revature.registration.repositories.FacultyRepository;
import com.revature.registration.repositories.StudentRepository;
import com.revature.registration.services.CourseServices;
import com.revature.registration.services.UserServices;
import com.revature.registration.web.filters.AuthFilter;
import com.revature.registration.web.security.JwtConfig;
import com.revature.registration.web.security.TokenGenerator;
import com.revature.registration.web.servlets.*;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.EnumSet;

public class ContextLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
        ObjectMapper objectMapper = new ObjectMapper();
        JwtConfig jwtConfig = new JwtConfig();
        TokenGenerator tokenGenerator = new TokenGenerator(jwtConfig);

        CourseRepository courseRepository = new CourseRepository();
        FacultyRepository facultyRepository = new FacultyRepository();
        StudentRepository studentRepository = new StudentRepository();

        CourseServices courseServices = new CourseServices(courseRepository);
        UserServices userServices = new UserServices(studentRepository,facultyRepository);

        AuthFilter authFilter = new AuthFilter(jwtConfig);
        HealthCheckServlet healthCheckServlet = new HealthCheckServlet();
        CourseServlet courseServlet = new CourseServlet(courseServices, userServices, objectMapper);
        RegistrationServlet registrationServlet = new RegistrationServlet(userServices, courseServices, objectMapper);
        StudentServlet studentServlet = new StudentServlet(userServices, courseServices, objectMapper);
        FacultyServlet facultyServlet = new FacultyServlet(userServices, objectMapper);
        AuthStudentServlet authStudentServlet = new AuthStudentServlet(userServices,objectMapper, tokenGenerator);
        AuthFacultyServlet authFacultyServlet = new AuthFacultyServlet(userServices,objectMapper, tokenGenerator);

        ServletContext servletContext = sce.getServletContext();
        servletContext.addFilter("AuthFilter", authFilter).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
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
