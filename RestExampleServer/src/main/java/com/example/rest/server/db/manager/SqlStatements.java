package com.example.rest.server.db.manager;

import com.example.rest.server.service.xmldeserializer.XmlDeserializer;
import com.example.rest.server.service.xmldeserializer.sql.Sql;
import com.example.rest.server.service.xmldeserializer.sql.statement.Statement;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cristina on 5/30/2017.
 */
public class SqlStatements {
    private static final Logger LOGGER = Logger.getLogger(SqlStatements.class);

    private Map<String, String> sqlQueries;

    public SqlStatements(String sqlFile) throws Exception {
        this.sqlQueries = new HashMap<>();

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(sqlFile);
        XmlDeserializer xmlDeserializer = new XmlDeserializer();
        Sql sql = xmlDeserializer.work(Sql.class, inputStream);

        if (sql != null) {
            for (Statement statement : sql.getStatements()) {
                String queryName = statement.getName();
                if (this.sqlQueries.containsKey(queryName)) {
                    LOGGER.error(String.format("Found duplicate SQL query name %s in file %s, skipping it...", queryName, sqlFile));
                }
                this.sqlQueries.put(queryName, statement.getSqlString());
            }
        } else {
            throw new Exception(String.format("Could not deserialize %s file!", sqlFile));
        }
    }

    public Map<String, String> getSqlQueries() {
        return this.sqlQueries;
    }

    public String getSqlQuery(String queryName) {
        return this.sqlQueries.get(queryName);
    }
}
