package com.revature.registration.repositories;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.revature.registration.models.Course;
import com.revature.registration.models.Faculty;
import com.revature.registration.models.Student;
import com.revature.registration.util.ConnectionFactory;
import com.revature.registration.util.exceptions.DataSourceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CourseRepository communicates between CourseServices and the Course Collection in the database. Its methods
 * allow for creating, reading, updating, and deleting data about Courses that can be registered for.
 */
public class CourseRepository{

    private final Logger logger = LogManager.getLogger(CourseRepository.class);

    /**
     * save takes in a Course and persists it to the database. it returns the Course that it just saved, including
     * the id MongoDB generates automatically ("_id" in MongoDB)
     * @param newCourse
     * @return
     */
    public Course save(Course newCourse) {
        try {
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase courseDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> courseCollection = courseDb.getCollection("course");
            Document newCourseDoc = new Document("number", newCourse.getNumber())
                    .append("name", newCourse.getName())
                    .append("description", newCourse.getDescription())
                    .append("professor", newCourse.getProfessor())
                    .append("capacity", newCourse.getCapacity())
                    .append("students", Arrays.asList(newCourse.getStudents()));

            courseCollection.insertOne(newCourseDoc);
            newCourse.setId(newCourseDoc.get("_id").toString());
            return newCourse;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred while trying to persist course to database",e);
        }
    }

    /**
     * findById takes in a Course's id and returns the object in the Course collection with that id. If no object
     * with that id exists, it returns null.
     * @param id
     * @return
     */
    public Course findById(String id) {
        try {
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase courseDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> courseCollection = courseDb.getCollection("course");
            Document query = new Document("_id", id);
            Document result = courseCollection.find(query).first();

            if (result ==  null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            Course course = mapper.readValue(result.toJson(), Course.class);
            course.setId(result.get("_id").toString());

            return course;
        } catch (JsonMappingException jme) {
            logger.debug(jme.getMessage());
            throw new DataSourceException("An exception occurred while mapping the Document",jme);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred while trying to find course by id",e);
        }
    }

    /**
     * findByNumber takes in a string that is a course number (i.e. "math 120", "cs 200") and finds the first course
     * associated with that number in the database. It returns the Course it finds, or null or no Courses were found.
     * @param number
     * @return
     */
    public Course findByNumber(String number) {
        try {
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase courseDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> courseCollection = courseDb.getCollection("course");
            Document query = new Document("number",number);
            Document result = courseCollection.find(query).first();

            if (result == null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            Course course = mapper.readValue(result.toJson(),Course.class);
            course.setId(result.get("_id").toString());

            return course;
        } catch (JsonMappingException jme) {
            logger.debug(jme.getMessage());
            throw new DataSourceException("An exception occurred while mapping the Document",jme);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred while trying to find course by number",e);
        }
    }

    /**
     * findByStudent takes in a Student and finds all Courses that they are registered for. It returns a List of
     * Courses, which is empty if a Student is not registered for any Courses
     * @param student
     * @return
     */
    public List<Course> findByStudent(Student student) {
        try {
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase courseDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> courseCollection = courseDb.getCollection("course");
            List<Document> result = courseCollection.find(Filters.in("students",student.getEmail())).into(new ArrayList<>());

            ObjectMapper mapper = new ObjectMapper();
            List<Course> registeredCourses = new ArrayList<>();

            for (Document d : result) {
                registeredCourses.add(mapper.readValue(d.toJson(), Course.class));
            }
            return registeredCourses;
        } catch (JsonMappingException jme) {
            logger.debug(jme.getMessage());
            throw new DataSourceException("An exception occurred while mapping the Document",jme);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred while trying to find courses that student is registered for",e);
        }
    }

    /**
     * findByFaculty finds all Courses taught by a particular Faculty. Those Courses are returned in a List,
     * which is empty if a Faculty does not teach any Courses.
     * @param faculty
     * @return
     */
    public List<Course> findByFaculty(Faculty faculty) {
        try {
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase courseDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> courseCollection = courseDb.getCollection("course");
            Document query = new Document("professor", faculty.getEmail());
            List<Document> result = courseCollection.find(query).into(new ArrayList<>());

            ObjectMapper mapper = new ObjectMapper();
            List<Course> taughtCourses = new ArrayList<>();

            for (Document d : result) {
                Course c = mapper.readValue(d.toJson(),Course.class);
                c.setId(d.get("_id").toString());
                taughtCourses.add(c);
            }
            return taughtCourses;
        } catch (JsonMappingException jme) {
            logger.debug(jme.getMessage());
            throw new DataSourceException("An exception occurred while mapping the Document",jme);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred while trying to find courses taught by faculty member",e);
        }
    }

    /**
     * findAll() returns the entire Course collection in a List.
     * @return
     */
    public List<Course> findAll() {
        try {
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase courseDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> courseCollection = courseDb.getCollection("course");
            List<Document> courseDocList = courseCollection.find().into(new ArrayList<>());
            List<Course> courseList = new ArrayList<>();

            ObjectMapper mapper = new ObjectMapper();

            for (Document d : courseDocList) {
                courseList.add(mapper.readValue(d.toJson(), Course.class));
            }
            return courseList;
        } catch (JsonMappingException jme) {
            logger.debug(jme.getMessage());
            throw new DataSourceException("An exception occurred while mapping the Document",jme);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred while trying to find all courses in the database",e);
        }
    }

    /**
     * update takes in a course number, which it uses to find the course to change the value
     * of a specified field to newValue.
     * @param currentNumber
     * @param field
     * @param newValue
     * @return
     */
    public boolean update(String currentNumber, String field, String newValue) {
        try {
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase courseDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> courseCollection = courseDb.getCollection("course");

//            if (field.equals("number") && courseCollection.find(Filters.eq("number", newValue)) != null) {
//                return false;
//            }
//
//            if (courseCollection.find(Filters.eq("number", currentNumber)) == null) {
//                return false;
//            }

            if (field.equals("capacity")) {
                int nv = Integer.parseInt(newValue);
                courseCollection.updateOne(Filters.eq("number", currentNumber), Updates.set(field, nv));
            } else {
                courseCollection.updateOne(Filters.eq("number", currentNumber), Updates.set(field, newValue));
            }
            return true;

        } catch (NumberFormatException nfe) {
            throw new DataSourceException("The capacity provided was not a number",nfe);
        } catch (Exception e){
            logger.debug(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred while trying to update a course",e);
        }
    }

    /**
     * addStudent registers a student, referenced by their email, for a course specified by a course number.
     * @param courseNumber
     * @param email
     * @return
     */
    public boolean addStudent(String courseNumber, String email) { //
        try {
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase courseDb = mongoClient.getDatabase("p0");
            MongoCollection courseCollection = courseDb.getCollection("course");

            courseCollection.updateOne(Filters.eq("number", courseNumber),
                    Updates.addToSet("students", email));
            return true;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred while trying to register student to course",e);
        }
    }

    /**
     * removeStudent unregisters a student, referenced by their email,
     * from a particular course, specified by a course number
     * @param courseNumber
     * @param email
     * @return
     */
    public boolean removeStudent(String courseNumber, String email) {
        try {
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase courseDb = mongoClient.getDatabase("p0");
            MongoCollection courseCollection = courseDb.getCollection("course");

            courseCollection.updateOne(Filters.eq("number", courseNumber), Updates.pull("students", email));
            return true;
        } catch (Exception e){
            logger.debug(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred while trying to unregister student from course",e);
        }
    }

    /**
     * deleteByNumber removes a Course from the course list, searching by its course number.
     * @param number
     * @return
     */
    public boolean deleteByNumber(String number) {
        try {
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase courseDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> courseCollection = courseDb.getCollection("course");

            if (courseCollection.find(Filters.eq("number", number)) == null) {
                return false;
            }
            courseCollection.deleteOne(Filters.eq("number", number));
            return true;
        } catch (Exception e){
            logger.debug(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred while trying to delete a course by number",e);
        }
    }
}
