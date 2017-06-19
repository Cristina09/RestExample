package com.example.rest.server.db.dao;

import com.example.rest.client.model.AuditEmbargoRecord;
import com.example.rest.client.model.EmbargoRecord;

import java.util.List;

/**
 * Created by Cristina on 5/30/2017.
 */
public interface EmbargoRecordDao {
    EmbargoRecord getEmbargoRecord(Integer firmId, String emailDomain);
    EmbargoRecord getEmbargoRecordById(Integer Id);
    List<EmbargoRecord> getEmbargoRecordsForFirmId(Integer firmId);
    List<EmbargoRecord> getEmbargoRecords();
    EmbargoRecord addEmbargoRecord(EmbargoRecord embargoRecord);
    Boolean updateEmbargoRecord(EmbargoRecord embargoRecord);
    Boolean deleteEmbargoRecord(Integer Id);
    AuditEmbargoRecord addAuditEmbargoRecord(AuditEmbargoRecord auditEmbargoRecord);
    Integer getLastInsertedId();
}
