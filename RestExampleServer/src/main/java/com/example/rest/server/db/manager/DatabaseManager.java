package com.example.rest.server.db.manager;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PooledDataSource;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.sql.SQLException;

/**
 * Created by Cristina on 5/30/2017.
 */
public class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class);


    private static final String SQL_STATEMENTS_FILE = "sql.xml";
    private static DatabaseManager instance;
    private static boolean initialized;

    private ComboPooledDataSource dataSource;
    private SqlStatements sqlStatements;

    private DatabaseManager(ComboPooledDataSource dataSource, SqlStatements sqlStatements) {
        this.dataSource = dataSource;
        this.sqlStatements = sqlStatements;
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    public static synchronized boolean init(String propertiesFile) {
        if (DatabaseManager.initialized) {
            return true;
        }

        //connection pool
        ComboPooledDataSource connectionPool;
        try {
            Configuration configuration = new PropertiesConfiguration(propertiesFile);
            String url = configuration.getString("database.courier.url");
            String driver = configuration.getString("database.courier.jdbc");
            String user = configuration.getString("database.courier.user");
            String password = configuration.getString("database.courier.password");
            DatabaseInfo databaseInfo = new DatabaseInfo(driver, url, user, password);

            connectionPool = DatabaseManager.initConnectionPool(databaseInfo);
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception initializing DatabaseManager w/ properties file [%s]: ", propertiesFile), exception);
            return false;
        }

        //sql statements
        SqlStatements sqlStatements;
        try {
            sqlStatements = new SqlStatements(SQL_STATEMENTS_FILE);
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception initializing DatabaseManager w/ SQL Statements file [%s]: ", SQL_STATEMENTS_FILE), exception);
            return false;
        }

        DatabaseManager.instance = new DatabaseManager(connectionPool, sqlStatements);
        DatabaseManager.initialized = true;
        return true;
    }

    public static void shutdown() {
        try {
            LOGGER.info("Shutting down DB pools...");
            for (Object pool: C3P0Registry.getPooledDataSources()) {
                PooledDataSource pds = (PooledDataSource) pool;
                DataSources.destroy(pds);
            }
        } catch(Exception exception) {
            LOGGER.error("Exception shutting down DB pools: ", exception);
        }
        DatabaseManager.initialized = false;
        DatabaseManager.instance = null;
    }

    private synchronized static ComboPooledDataSource initConnectionPool(DatabaseInfo databaseInfo) {
        ComboPooledDataSource pool;
        try {
            pool = new ComboPooledDataSource();
            pool.setDriverClass(databaseInfo.getDriver());
            pool.setJdbcUrl(databaseInfo.getUrl());
            pool.setUser(databaseInfo.getUser());
            pool.setPassword(databaseInfo.getPassword());
            pool.setUnreturnedConnectionTimeout(250);
            pool.setDebugUnreturnedConnectionStackTraces(true);
            pool.setMinPoolSize(10);
            pool.setMaxPoolSize(30);
            pool.setTestConnectionOnCheckout(false);
            pool.setTestConnectionOnCheckin(true);
            pool.setPreferredTestQuery("SELECT 1");
            pool.setIdleConnectionTestPeriod(300);
        } catch (Exception e) {
            LOGGER.error(String.format("Exception initializing Connection Pool for URL [%s]: ", databaseInfo.getUrl()), e);
            return null;
        }
        LOGGER.info(String.format("DB Name: %s", pool.getDataSourceName()));
        return pool;
    }

    public DBConnection getConnection() throws SQLException {
        if (!DatabaseManager.initialized) {
            LOGGER.error("Tried to get DB connection while DatabaseManager not initialized, returning NULL...");
            return null;
        }

        try {
            return new DBConnection(this.dataSource.getConnection(), this.sqlStatements);
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception getting DB Connection: %s", exception.getMessage()), exception);
        }
        return null;
    }
}
