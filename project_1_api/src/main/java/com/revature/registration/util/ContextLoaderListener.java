package com.revature.registration.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

    MongoClient mongoClient = ConnectionFactory.getInstance().getConnection();

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionFactory.getInstance().cleanUp();
    }
}
