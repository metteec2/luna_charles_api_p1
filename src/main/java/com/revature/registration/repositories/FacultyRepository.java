package com.revature.registration.repositories;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.revature.registration.models.Faculty;
import com.revature.registration.util.ConnectionFactory;
import com.revature.registration.util.exceptions.DataSourceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

/**
 * The FacultyRepository Class connects with the collection in the database where Faculty are stored. Its methods
 * are used by UserServices to communicate between the user and the database.
 */
public class FacultyRepository implements CrudRepository<Faculty>{

    private final Logger logger = LogManager.getLogger(FacultyRepository.class);

    /**
     * The save method takes in a Faculty object and persists that faculty to the database. It returns the Faculty
     * that it saved, including the id that MongoDB assigned it ("_id" in MongoDB)
     * @param newFaculty
     * @return
     */
    @Override
    public Faculty save(Faculty newFaculty) {

        MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
        MongoDatabase facultyDb = mongoClient.getDatabase("p0");
        MongoCollection<Document> facultyCollection = facultyDb.getCollection("faculty");
        Document newFacultyDoc = new Document("firstName", newFaculty.getFirstName())
                .append("lastName",newFaculty.getLastName())
                .append("email",newFaculty.getEmail())
                .append("password",newFaculty.getPassword());

        facultyCollection.insertOne(newFacultyDoc);
        newFaculty.setId(newFacultyDoc.get("_id").toString());

        return newFaculty;
    }

    /**
     * findById takes in the id of a Faculty and returns the Faculty in the database with the matching id. If no such
     * Faculty exists, findById returns null.
     * @param id
     * @return
     */
    @Override
    public Faculty findById(String id) {

        try {
            // TODO obfuscate dbName and collectionName with properties
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase facultyDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> facultyCollection = facultyDb.getCollection("faculty");
            Document queryDoc = new Document("_id", id);
            Document returnDoc = facultyCollection.find(queryDoc).first();

            if (returnDoc == null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            Faculty faculty = mapper.readValue(returnDoc.toJson(), Faculty.class);
            faculty.setId(returnDoc.get("_id").toString());

            return faculty;
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
     * for a Faculty with matching credentials. If a Faculty with matching credentials is found, then it is returned.
     * Otherwise, findByCredentials returns null.
     * @param email
     * @param password
     * @return
     */
    public Faculty findByCredentials(String email, String password) {

        try {
            // TODO obfuscate dbName and collectionName with properties
            MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
            MongoDatabase facultyDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> facultyCollection = facultyDb.getCollection("faculty");
            Document queryDoc = new Document("email", email).append("password", password);
            Document authDoc = facultyCollection.find(queryDoc).first();

            if (authDoc == null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            Faculty faculty = mapper.readValue(authDoc.toJson(), Faculty.class);
            faculty.setId(authDoc.get("_id").toString());
            System.out.println(faculty);
            return faculty;
        } catch (JsonMappingException jme) {
            logger.debug(jme.getMessage());
            throw new DataSourceException("An exception occurred while mapping the Document",jme);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred",e);
        }
    }

    /**
     * update takes in a Faculty object to update, along with the field to update and the newValue to update it to.
     * update returns false if it tries to update an email to one that is already taken, otherwise it returns true.
     * @param updateFaculty
     * @param field
     * @param newValue
     * @return
     */
    @Override
    public boolean update(Faculty updateFaculty,String field,String newValue) {

        MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
        MongoDatabase facultyDb = mongoClient.getDatabase("p0");
        MongoCollection<Document> facultyCollection = facultyDb.getCollection("faculty");
        Document queryDoc = new Document(field,newValue);

        if (field.equals("email") && facultyCollection.find(queryDoc) != null) {
            return false;
        }

        facultyCollection.updateOne(Filters.eq("email",updateFaculty.getEmail()), Updates.set(field,newValue));

        return true;
    }

    /**
     * deleteById takes in a Faculty's id and deletes the Faculty with the matching id from the database. It returns
     * true upon successful deletion, or if no document with a matching id was in the database.
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(String id) {

        MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();
        MongoDatabase facultyDb = mongoClient.getDatabase("p0");
        MongoCollection<Document> facultyCollection =  facultyDb.getCollection("faculty");

        if (facultyCollection.find(Filters.eq("_id",id)) == null) {
            return false;
        }

        facultyCollection.deleteOne(Filters.eq("_id",id));
        return true;
    }
}
