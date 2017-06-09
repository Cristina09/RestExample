package com.example.rest.server.db.manager;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Cristina on 5/30/2017.
 */
public class DBConnection {
    private static final Logger LOGGER = Logger.getLogger(DBConnection.class);

    private Connection connection;
    private SqlStatements sqlStatements;

    public DBConnection(Connection connection, SqlStatements sqlStatements) {
        this.connection = connection;
        this.sqlStatements = sqlStatements;
    }

    private PreparedStatement prepareStatement(String sqlStatement) throws SQLException {
        PreparedStatement statement;
        if ((0 == sqlStatement.indexOf("call")) || (1 == sqlStatement.indexOf("call"))) {
            statement = this.connection.prepareCall(sqlStatement);
        } else {
            statement = this.connection.prepareStatement(sqlStatement);
        }
        return statement;
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return connection.prepareCall(sql);
    }

    public ResultSet executeQuery(Query query) throws SQLException {
        String queryName = query.getName();
        Object[] queryParameters = query.getParameters();
        return this.executeQuery(queryName, queryParameters);
    }

    public ResultSet executeQuery(String name, Object... params) throws SQLException {
        String sqlStatement = this.sqlStatements.getSqlQueries().get(name);
        if (null == sqlStatement) {
            LOGGER.error(String.format("SQL Statement %s does not exist in connection cache!", name));
            return null;
        }
        PreparedStatement statement = prepareStatement(sqlStatement);
        if (null != params) {
            for (int i = 0; i < params.length; i++) {
                if (null == params[i]) {
                    statement.setNull(i + 1, Types.CHAR);
                    continue;
                }
                String className = params[i].getClass().getName();
                if ("java.lang.String".equals(className)) {
                    statement.setString(i + 1, (String) params[i]);
                } else if ("java.lang.Integer".equals(className)) {
                    statement.setInt(i + 1, (Integer) params[i]);
                } else if ("java.lang.Long".equals(className)) {
                    statement.setLong(i + 1, (Long) params[i]);
                } else if ("java.lang.Float".equals(className)) {
                    statement.setFloat(i + 1, (Float) params[i]);
                } else if ("java.lang.Double".equals(className)) {
                    statement.setDouble(i + 1, (Double) params[i]);
                } else if ("java.sql.Date".equals(className)) {
                    statement.setDate(i + 1, (Date) params[i]);
                } else if ("java.util.Date".equals(className)) {
                    statement.setTimestamp(i + 1, new Timestamp(((java.util.Date) params[i]).getTime()));
                } else {
                    LOGGER.error("Invalid parameter object class: " + className);
                    return null;
                }
            }
        }
        try {
            LOGGER.debug(String.format("Executing query %s with parameters: %s", name, (params != null ? Arrays.toString(params) : "[]")));
            return statement.executeQuery();
        } finally {
            try {
                for (SQLWarning warning = statement.getWarnings(); warning != null; warning = warning.getNextWarning()) {
                    LOGGER.warn(String.format("Got Warning on MySQL Statement %s: %s (Code: %s)!", name, warning.getMessage(), warning.getErrorCode()));
                }
                statement.clearWarnings();
                statement.clearParameters();
            } catch (SQLException e) {
                LOGGER.error("Exception handling MySQL warnings: ", e);
            }
        }
    }

    public boolean executeTransaction(Query... queries) throws SQLException {
        if ((null == queries) || (0 == queries.length)) {
            return true;
        }
        if (1 == queries.length) {
            return executeUpdate(queries[0].getName(),queries[0].getParameters());
        }
        this.connection.setAutoCommit(false);
        try {
            for (Query query: queries) {
                this.executeUpdate(query);
            }
            return true;
        } catch (SQLException e) {
            this.connection.rollback();
            throw e;
        } finally {
            this.connection.setAutoCommit(true);
        }
    }

    public boolean executeTransaction(List<Query> queries) throws SQLException {
        Query[] queryArray = queries.toArray(new Query[queries.size()]);
        return this.executeTransaction(queryArray);
    }

    public boolean executeUpdate(Query query) throws SQLException {
        String queryName = query.getName();
        Object[] queryParameters = query.getParameters();
        return this.executeUpdate(queryName, queryParameters);
    }

    public boolean executeUpdate(String name, Object... params) throws SQLException {
        int response = -1;
        String sqlStatement = this.sqlStatements.getSqlQueries().get(name);
        if (sqlStatement == null) {
            throw new SQLException(String.format("SQL Statement %s does not exist in connection cache!", name));
        }
        PreparedStatement statement = prepareStatement(sqlStatement);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (null == params[i]) {
                    statement.setNull(i + 1, Types.CHAR);
                    continue;
                }
                String className = params[i].getClass().getName();
                if ("java.lang.String".equals(className)) {
                    statement.setString(i + 1, (String) params[i]);
                } else if ("java.lang.Integer".equals(className)) {
                    statement.setInt(i + 1, (Integer) params[i]);
                } else if ("java.lang.Long".equals(className)) {
                    statement.setLong(i + 1, (Long) params[i]);
                } else if ("java.lang.Float".equals(className)) {
                    statement.setFloat(i + 1, (Float) params[i]);
                } else if ("java.lang.Double".equals(className)) {
                    statement.setDouble(i + 1, (Double) params[i]);
                } else if ("java.sql.Date".equals(className)) {
                    statement.setDate(i + 1, (Date) params[i]);
                } else if ("java.util.Date".equals(className)) {
                    statement.setTimestamp(i + 1, new Timestamp(((java.util.Date) params[i]).getTime()));
                } else {
                    throw new SQLException(String.format("Invalid parameter object class: %s!", className));
                }
            }
        }
        try {
            LOGGER.debug(String.format("Executing update %s with parameters: %s", name, (params != null ? Arrays.toString(params) : "[]")));
            response = statement.executeUpdate();
        } finally {
            try {
                for (SQLWarning warning = statement.getWarnings(); warning != null; warning = warning.getNextWarning()) {
                    LOGGER.warn(String.format("Got Warning on MySQL Update Statement %s: %s (Code: %s)!", name, warning.getMessage(), warning.getErrorCode()));
                }
                statement.clearWarnings();
                statement.clearParameters();
            } catch(SQLException e) {
                LOGGER.error("Exception handling MySQL warnings: ", e);
            }
        }
        return response > 0;
    }

    public void close() throws SQLException {
        this.connection.close();
    }
}
