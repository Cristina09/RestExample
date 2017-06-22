package com.example.rest.server.db.dao;

import com.example.rest.client.model.AuditEmbargoRecord;
import com.example.rest.client.model.EmbargoRecord;
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
public class EmbargoRecordDaoImpl implements EmbargoRecordDao {
    private static final Logger LOGGER = Logger.getLogger(EmbargoRecordDaoImpl.class);

    private DBConnection connection;

    public EmbargoRecordDaoImpl(DBConnection connection) {
        this.connection = connection;
    }

    @Override
    public EmbargoRecord getEmbargoRecord(Integer firmId, String emailDomain) {
        EmbargoRecord record = null;
        try {
            ResultSet rs = this.connection.executeQuery("getEmbargoRecord", firmId, emailDomain);
            if (rs.next()) {
                record = this.buildObject(rs);
            }
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception getting EmbargoRecord from DB for firmID [%d] and email Domain [%s]: ", firmId, emailDomain), exception);
        }
        return record;
    }

    @Override
    public EmbargoRecord getEmbargoRecordById(Integer Id) {
        EmbargoRecord record = null;
        try {
            ResultSet rs = this.connection.executeQuery("getEmbargoRecordById", Id);
            if (rs.next()) {
                record = this.buildObject(rs);
            }
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception getting EmbargoRecord from DB for ID [%d]: ", Id), exception);
        }
        return record;
    }

    @Override
    public List<EmbargoRecord> getEmbargoRecordsForFirmId(Integer firmId) {
        List<EmbargoRecord> records = new ArrayList<EmbargoRecord>();
        try {
            ResultSet rs = this.connection.executeQuery("getEmbargoRecordsForFirmId", firmId);
            while (rs.next()) {
                EmbargoRecord record = this.buildObject(rs);
                if (null != record) {
                    records.add(record);
                }
            }
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception getting EmbargoRecord from DB for firmID [%d]: ", firmId), exception);
        }
        return records;
    }

    @Override
    public List<EmbargoRecord> getEmbargoRecords() {
        List<EmbargoRecord> records = new ArrayList<EmbargoRecord>();
        try {
            ResultSet rs = this.connection.executeQuery("getEmbargoRecords");
            while (rs.next()) {
                EmbargoRecord record = this.buildObject(rs);
                if (null != record) {
                    records.add(record);
                }
            }
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception getting EmbargoRecord from DB."), exception);
        }
        return records;
    }

    @Override
    public EmbargoRecord addEmbargoRecord(EmbargoRecord embargoRecord) {
        List<Query> queries = new ArrayList<>();
        queries.add(new Query("insertEmbargoRecord", embargoRecord.getFirmId(), embargoRecord.getEmailDomain(), embargoRecord.getFullAnonDays(), embargoRecord.getFirmVisibleDays(), embargoRecord.getFullVisibleDays()));

        try {
            if (!this.connection.executeTransaction(queries)){
                LOGGER.error(String.format("Could not save EmbargoRecord for id [%d]!", embargoRecord.getId()));
                return null;
            }
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception saving EmbargoRecord for id [%d]: ", embargoRecord.getId()), exception);
            return null;
        }

        return embargoRecord;
    }

    @Override
    public Boolean updateEmbargoRecord(EmbargoRecord embargoRecord) {
        try {
            Boolean rs = this.connection.executeUpdate("updateEmbargoRecord", embargoRecord.getFirmId(), embargoRecord.getEmailDomain(), embargoRecord.getFullAnonDays(), embargoRecord.getFirmVisibleDays(), embargoRecord.getFullVisibleDays(), embargoRecord.getId());
            return rs;
        }catch (Exception exception) {
            LOGGER.error(String.format("Exception updating EmbargoRecord for ID [%d]: ", embargoRecord.getId()), exception);
            return false;
        }
    }

    @Override
    public Boolean deleteEmbargoRecord(Integer Id) {
        try {
            Boolean rs = this.connection.executeUpdate("deleteEmbargoRecord", Id);
            return rs;
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception deleting EmbargoRecord for ID [%d]: ", Id), exception);
            return false;
        }
    }

    private EmbargoRecord buildObject(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        Integer firmId = rs.getInt("firm_id");
        String emailDomain = rs.getString("email_domain");
        Integer fullAnonDays = rs.getInt("full_anon_days");
        Integer firmVisibleDays = rs.getInt("firm_visible_days");
        Integer fullVisibleDays = rs.getInt("full_visible_days");

        return new EmbargoRecord(id, firmId, emailDomain, fullAnonDays, firmVisibleDays, fullVisibleDays);
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
