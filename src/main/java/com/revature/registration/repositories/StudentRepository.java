package com.revature.registration.repositories;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.revature.registration.models.Student;
import com.revature.registration.util.ConnectionFactory;
import com.revature.registration.util.exceptions.DataSourceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

/**
 * The StudentRepository Class connects with the collection in the database where Students are stored. Its methods are
 * used by UserServices to communicate between the user and the database.
 */
public class StudentRepository implements CrudRepository<Student>{

    private final Logger logger = LogManager.getLogger(StudentRepository.class);

    /**
     * The save method takes in a Student object and persists that Student to the database. It returns the Student
     * that it saved, including the id that MongoDB assigned it ("_id" in MongoDB)
     * @param newStudent
     * @return
     */
    @Override
    public Student save(Student newStudent) {
        MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
        MongoDatabase studentDb = mongoClient.getDatabase("p0");
        MongoCollection<Document> studentCollection = studentDb.getCollection("student");
        Document newStudentDoc = new Document("firstName", newStudent.getFirstName())
                .append("lastName",newStudent.getLastName())
                .append("email",newStudent.getEmail())
                .append("password",newStudent.getPassword());

        studentCollection.insertOne(newStudentDoc);
        newStudent.setId(newStudentDoc.get("_id").toString());

        return newStudent;
    }

    /**
     * findById takes in the id of a Student and returns the Student in the database with the matching id. If no such
     * Student exists, findById returns null.
     * @param id
     * @return
     */
    @Override
    public Student findById(String id) {
        try {
            // TODO obfuscate dbName and collectionName with properties
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase studentDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> studentCollection = studentDb.getCollection("student");
            Document queryDoc = new Document("_id", id);
            Document returnDoc = studentCollection.find(queryDoc).first();

            ObjectMapper mapper = new ObjectMapper();
            Student student = mapper.readValue(returnDoc.toJson(), Student.class);
            student.setId(returnDoc.get("_id").toString());

            return student;
        } catch (JsonMappingException jme) {
            logger.debug(jme.getMessage());
            throw new DataSourceException("An exception occurred while mapping the Document",jme);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred",e);
        }
    }

    /**
     * findByEmail takes in a Student's email and queries the database for a student with that email. If no such
     * Student is found, findByEmail returns null.
     * @param email
     * @return
     */
    public Student findByEmail(String email) {
        try {
            // TODO obfuscate dbName and collectionName with properties
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase studentDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> studentCollection = studentDb.getCollection("student");
            Document queryDoc = new Document("email", email);
            Document returnDoc = studentCollection.find(queryDoc).first();

            if (returnDoc == null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            Student student = mapper.readValue(returnDoc.toJson(), Student.class);
            student.setId(returnDoc.get("_id").toString());

            return student;
        } catch (JsonMappingException jme) {
            logger.debug(jme.getMessage());
            throw new DataSourceException("An exception occurred while mapping the Document",jme);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred",e);
        }
    }

    /**
     * findByCredentials takes in two String arguments: an email and a password. Using these is queries the database
     * for a Student with matching credentials. If a Student with matching credentials is found, then it is returned.
     * Otherwise, findByCredentials returns null.
     * @param email
     * @param password
     * @return
     */
    public Student findByCredentials(String email,String password) {
        try {
            // TODO obfuscate dbName and collectionName with properties
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase studentDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> studentCollection = studentDb.getCollection("student");
            Document queryDoc = new Document("email", email).append("password", password);
            Document authDoc = studentCollection.find(queryDoc).first();

            if (authDoc == null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            Student student = mapper.readValue(authDoc.toJson(), Student.class);
            student.setId(authDoc.get("_id").toString());

            return student;
        } catch (JsonMappingException jme) {
            logger.debug(jme.getMessage());
            throw new DataSourceException("An exception occurred while mapping the Document",jme);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred",e);
        }
    }

    /**
     * update takes in a Student object to update, along with the field to update and the newValue to update it to.
     * update returns false if it tries to update an email to one that is already taken, otherwise it returns true.
     * @param updateStudent
     * @param field
     * @param newValue
     * @return
     */
    @Override
    public boolean update(Student updateStudent,String field,String newValue) {

        MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
        MongoDatabase studentDb = mongoClient.getDatabase("p0");
        MongoCollection<Document> studentCollection = studentDb.getCollection("student");
        Document queryDoc = new Document(field,newValue);

        // TODO create if statements with other potential problems
        if (field.equals("email") && studentCollection.find(queryDoc) != null) {
            return false;
        }

        studentCollection.updateOne(Filters.eq("email",updateStudent.getEmail()), Updates.set(field,newValue));

        return true;
    }

    /**
     * deleteById takes in a Student's id and deletes the Student with the matching id from the database. It returns
     * true upon successful deletion, or if no document with a matching id was in the database.
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(String id) {
        // TODO remove student from courses

        MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
        MongoDatabase studentDb = mongoClient.getDatabase("p0");
        MongoCollection<Document> studentCollection =  studentDb.getCollection("student");

        if (studentCollection.find(Filters.eq("_id",id)) == null) {
            return false;
        }

        studentCollection.deleteOne(Filters.eq("_id",id));
        return true;
    }
}
