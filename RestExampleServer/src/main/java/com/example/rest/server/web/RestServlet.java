package com.example.rest.server.web;

import com.example.rest.server.db.manager.DatabaseManager;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by Cristina on 5/31/2017.
 */
public class RestServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RestServlet.class);

    private static final String CONFIG_FILE = "global.properties";

    private static String authenticationToken;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (!DatabaseManager.init(CONFIG_FILE)) {
            LOGGER.error("Couldn't initialize database connection!");
        }

        //create authentication token
        authenticationToken = this.createAuthenticationToken();
    }

    private String createAuthenticationToken() {
        String tokenString = null;
        try {
            Configuration configuration = new PropertiesConfiguration(CONFIG_FILE);
            String username = configuration.getString("authentication.user");
            String password = configuration.getString("authentication.password");
            tokenString = username + ":" + password;
        } catch (Exception exception) {
            //todo:logger
        }
        return tokenString;
    }

    @Override
    public void destroy() {
        LOGGER.info("Shutting down DatabaseManager ...");
        DatabaseManager.shutdown();
        LOGGER.info("RestServlet shut down.");
        super.destroy();
    }

    public static String getAuthenticationToken() {
        return authenticationToken;
    }
}
