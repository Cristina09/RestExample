package com.example.rest.server.db.dao;

import com.example.rest.client.model.AuditEmbargoRecord;
import com.example.rest.server.db.manager.DBConnection;
import com.example.rest.server.db.manager.Query;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Cristina on 5/30/2017.
 */
public class AuditEmbargoEmbargoRecordDaoImpl implements AuditEmbargoRecordDao {
    private static final Logger LOGGER = Logger.getLogger(AuditEmbargoEmbargoRecordDaoImpl.class);

    private DBConnection connection;

    public AuditEmbargoEmbargoRecordDaoImpl(DBConnection connection) {
        this.connection = connection;
    }

    @Override
    public AuditEmbargoRecord addAuditEmbargoRecord(AuditEmbargoRecord auditEmbargoRecord) {
        List<Query> queries = new ArrayList<>();
        queries.add(new Query("insertAuditEmbargoRecords", auditEmbargoRecord.getFirmId(), auditEmbargoRecord.getEmailDomain(),auditEmbargoRecord.getSource(), auditEmbargoRecord.getUserEmail(), auditEmbargoRecord.getFullAnonDays(), auditEmbargoRecord.getFirmVisibleDays(), auditEmbargoRecord.getFullVisibleDays(), auditEmbargoRecord.getAction(), auditEmbargoRecord.getRecordId()));

        try {
            if (!this.connection.executeTransaction(queries)){
                LOGGER.error(String.format("Could not save AuditEmbargoRecord for id [%d]!", auditEmbargoRecord.getId()));
                return null;
            }
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception saving AuditEmbargoRecord for id [%d]: ", auditEmbargoRecord.getId()), exception);
            return null;
        }

        return auditEmbargoRecord;
    }

    @Override
    public List<AuditEmbargoRecord> getAuditEmbargoRecordsForId(Integer Id) {
        List<AuditEmbargoRecord> records = new ArrayList<>();
        try {
            ResultSet rs = this.connection.executeQuery("getAuditEmbargoRecordsForId", Id);
            while (rs.next()) {
                AuditEmbargoRecord record = this.buildAuditObject(rs);
                if (null != record) {
                    records.add(record);
                }
            }
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception getting EmbargoRecord from DB for firmID [%d]: ", Id), exception);
        }
        return records;
    }

    private AuditEmbargoRecord buildAuditObject(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        Integer firmId = rs.getInt("firm_id");
        String emailDomain = rs.getString("email_domain");
        String source = rs.getString("source");
        String userEmail = rs.getString("user_email");
        Integer fullAnonDays = rs.getInt("full_anon_days");
        Integer firmVisibleDays = rs.getInt("firm_visible_days");
        Integer fullVisibleDays = rs.getInt("full_visible_days");
        String action = rs.getString("action");
        Integer recordId = rs.getInt("record_id");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        Date date = new Date();
        String auditDate = rs.getString("date");
        if (auditDate != null){
            try {
                date = dateFormat.parse(auditDate);
            }catch (Exception exception){
                LOGGER.error(String.format("Exception creating EmbargoRecord with id [%d] because of the date from field", id),exception);
                return null;
            }
        }

        return new AuditEmbargoRecord(id, firmId, emailDomain, source, userEmail, fullAnonDays, firmVisibleDays, fullVisibleDays, action, recordId, date);
    }

    @Override
    public Integer getLastInsertedId(){
        Integer id = null;
        try {
            ResultSet rs= this.connection.executeQuery("getLastInsertId");
            if (rs.next()){
                id = rs.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
