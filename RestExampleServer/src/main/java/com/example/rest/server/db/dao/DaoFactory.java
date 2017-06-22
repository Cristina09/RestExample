package com.example.rest.server.db.dao;

import com.example.rest.server.db.manager.DBConnection;
import com.example.rest.server.db.manager.DatabaseManager;
import org.apache.log4j.Logger;

import java.sql.SQLException;

/**
 * Created by Cristina on 5/30/2017.
 */
public class DaoFactory {
    private static final Logger LOGGER = Logger.getLogger(DaoFactory.class);

    private DBConnection connection;

    public DaoFactory () throws SQLException, NullPointerException {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException exception) {
            LOGGER.error("Exception closing DB Connection: ", exception);
        }
    }

    public EmbargoRecordDao createEmbargoDao() { return new EmbargoRecordDaoImpl(this.connection); }
    public AuditEmbargoRecordDao createAuditEmbargoDao() { return new AuditEmbargoEmbargoRecordDaoImpl(this.connection); }
}
