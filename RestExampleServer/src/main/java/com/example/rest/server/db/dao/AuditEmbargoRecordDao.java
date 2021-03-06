package com.example.rest.server.db.dao;

import com.example.rest.client.model.AuditEmbargoRecord;
import com.example.rest.client.model.EmbargoRecord;

import java.util.List;

/**
 * Created by Cristina on 5/30/2017.
 */
public interface AuditEmbargoRecordDao {
    AuditEmbargoRecord addAuditEmbargoRecord(AuditEmbargoRecord auditEmbargoRecord);
    Integer getLastInsertedId();
    List<AuditEmbargoRecord> getAuditEmbargoRecordsForId(Integer Id);
}
