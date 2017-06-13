package com.example.rest.server.db.dao;

import com.example.rest.client.model.EmbargoRecord;
import com.example.rest.server.db.manager.DBConnection;
import com.example.rest.server.db.manager.Query;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
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
            LOGGER.error(String.format("Exception getting Embargo Record from DB for firmID [%d] and email Domain [%s]: ", firmId, emailDomain), exception);
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
            LOGGER.error(String.format("Exception getting Embargo Record from DB for firmID [%d]: ", firmId), exception);
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
            LOGGER.error(String.format("Exception getting Embargo Record from DB."), exception);
        }
        return records;
    }

    @Override
    public EmbargoRecord addEmbargoRecord(EmbargoRecord embargoRecord) {
        List<Query> queries = new ArrayList<>();
        queries.add(new Query("deleteEmbargoRecord", embargoRecord.getFirmId(), embargoRecord.getEmailDomain()));
        queries.add(new Query("insertEmbargoRecord", embargoRecord.getFirmId(), embargoRecord.getEmailDomain(), embargoRecord.getFullAnonDays(), embargoRecord.getFirmVisibleDays(), embargoRecord.getFullVisibleDays(), embargoRecord.getActiveFrom(), embargoRecord.getActiveTo()));

        try {
            if (!this.connection.executeTransaction(queries)){
                LOGGER.error(String.format("Could not save EmbargoRecord for firm [%d]!", embargoRecord.getFirmId()));
                return null;
            }
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception saving EmbargoRecord for firm [%d]: ", embargoRecord.getFirmId()), exception);
            return null;
        }

        return embargoRecord;
    }

    @Override
    public Boolean deleteEmbargoRecord(Integer firmId, String emailDomain) {
        try {
            Boolean rs = this.connection.executeUpdate("deleteEmbargoRecord", firmId, emailDomain);
            return rs;
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception deleting Embargo Record for firmID [%d] and email Domain [%s]: ", firmId, emailDomain), exception);
            return false;
        }
    }

    @Override
    public Boolean deleteEmbargoRecords(Integer firmId) {
        try {
            Boolean rs = this.connection.executeUpdate("deleteEmbargoRecords", firmId);
            return rs;
        } catch (Exception exception) {
            LOGGER.error(String.format("Exception deleting Embargo Record for firmID [%d]: ", firmId), exception);
            return false;
        }
    }

    private EmbargoRecord buildObject(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        Integer firmId = rs.getInt("firm_id");
        String emailDomain = rs.getString("email_domain");
        Integer fullanonDays = rs.getInt("full_anon_days");
        Integer firmVisibleDays = rs.getInt("firm_visible_days");
        Integer fullVisibleDays = rs.getInt("full_visible_days");
        String dateFrom = rs.getString("active_from");
        //todo: convert String from db into date
        Date activeFrom = new Date();
        String dateTo = rs.getString("active_to");
        //todo: convert String from db into date
        Date activeTo = new Date();
        return new EmbargoRecord(id, firmId, emailDomain, fullanonDays, firmVisibleDays, fullVisibleDays, activeFrom, activeTo);
    }
}
