package com.github.project1.common.datasource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionFactory {

    private static Logger logger = LogManager.getLogger(ConnectionFactory.class);

    private static ConnectionFactory connFactory;
    private Properties dbProps = new Properties();

    private ConnectionFactory() {
        try {
            Class.forName("org.postgresql.Driver");
            dbProps.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            logger.fatal("There was a problem reading from the properties file at {}, error message: {}", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Could not read from properties file.", e);
        } catch (ClassNotFoundException e) {
            logger.fatal("There was a problem loading the JDBC driver at {}, error message: {}", LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("failed to load PostgreSQL JDBC driver.", e);
        }
    }

    public static ConnectionFactory getInstance() {
        if (connFactory == null) {
            connFactory = new ConnectionFactory();
            logger.info("New connection established at {}", LocalDateTime.now());
        }
        return connFactory;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbProps.getProperty("db-url"), dbProps.getProperty("db-username"), dbProps.getProperty("db-password"));
    }
    
}
