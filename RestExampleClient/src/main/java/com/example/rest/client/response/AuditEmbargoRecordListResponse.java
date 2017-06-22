package com.example.rest.client.response;

import com.example.rest.client.model.AuditEmbargoRecord;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Cristina on 6/6/2017.
 */
@XmlRootElement
public class AuditEmbargoRecordListResponse {
    private List<AuditEmbargoRecord> auditEmbargoRecords;
    private ErrorCode errorCode;

    private AuditEmbargoRecordListResponse() {
    }

    public AuditEmbargoRecordListResponse(List<AuditEmbargoRecord> auditEmbargoRecords){
        this(auditEmbargoRecords, null);
    }

    public AuditEmbargoRecordListResponse(ErrorCode errorCode){
        this(null, errorCode);
    }

    public AuditEmbargoRecordListResponse(List<AuditEmbargoRecord> auditEmbargoRecords, ErrorCode errorCode){
        this.auditEmbargoRecords = auditEmbargoRecords;
        this.errorCode = errorCode;
    }

    public List<AuditEmbargoRecord> getAuditEmbargoRecords() {
        return auditEmbargoRecords;
    }

    public void setAuditEmbargoRecords(List<AuditEmbargoRecord> auditEmbargoRecords) {
        this.auditEmbargoRecords = auditEmbargoRecords;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public enum ErrorCode {
        RECORDS_NOT_FOUND("Could not find audit records!"),
        DATABASE_EXCEPTION("Could not initiate DaoFactory!"),
        ;

        private final String message;

        ErrorCode(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
