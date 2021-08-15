package com.revature.registration.util;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.revature.registration.util.exceptions.DataSourceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * ConnectionFactory is a singleton class that houses connections to the database.
 */
public class ConnectionFactory {

    private final Logger logger = LogManager.getLogger(ConnectionFactory.class);
    private final MongoClient mongoClient;
    private static final ConnectionFactory connectionFactory = new ConnectionFactory();

    private ConnectionFactory() {
        Properties appProperties = new Properties();

        try {

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            appProperties.load(loader.getResourceAsStream("app.properties"));

            String ipAddress = appProperties.getProperty("ipAddress");
            int port = Integer.parseInt(appProperties.getProperty("port"));
            String username = appProperties.getProperty("username");
            String dbName = appProperties.getProperty("dbName");
            char[] password = appProperties.getProperty("password").toCharArray();

            List<ServerAddress> hosts = Collections.singletonList(new ServerAddress(ipAddress,port));
            MongoCredential credentials = MongoCredential.createScramSha1Credential(username,dbName,password);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyToClusterSettings(builder -> builder.hosts(hosts))
                    .credential(credentials).build();

            this.mongoClient = MongoClients.create(settings);

        } catch (FileNotFoundException fnfe) {
            logger.error(fnfe.getMessage());
            logger.debug("Unable to load database properties file");
            throw new DataSourceException("Unable to load database properties file",fnfe);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.debug("an unexpected problem occurred when establishing database connection");
            throw new DataSourceException("An unexpected problem occurred", e);
        }
    }

    /**
     * cleanUp() closes the mongo client, which is required for ideal shutdown.
     */
    public void cleanUp() {
        mongoClient.close();
    }

    /**
     * getInstance returns the instance of connectionFactory so that other methods can be called on it.
     * @return
     */
    public static ConnectionFactory getInstance() {
        return connectionFactory;
    }

    /**
     * getConnection() returns the connection to the mongo database that ConnectionFactory set up.
     * @return
     */
    public MongoClient getConnection() {
        return mongoClient;
    }
}
