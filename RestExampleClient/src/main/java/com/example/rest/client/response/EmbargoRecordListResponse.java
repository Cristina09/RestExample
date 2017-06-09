package com.example.rest.client.response;

import com.example.rest.client.model.EmbargoRecord;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Cristina on 6/6/2017.
 */
@XmlRootElement
public class EmbargoRecordListResponse {
    private List<EmbargoRecord> embargoRecords;
    private ErrorCode errorCode;

    private EmbargoRecordListResponse() {
    }

    public EmbargoRecordListResponse(List<EmbargoRecord> embargoRecords){
        this(embargoRecords, null);
    }

    public EmbargoRecordListResponse(ErrorCode errorCode){
        this(null, errorCode);
    }

    public EmbargoRecordListResponse(List<EmbargoRecord> embargoRecords, ErrorCode errorCode){
        this.embargoRecords = embargoRecords;
        this.errorCode = errorCode;
    }

    public List<EmbargoRecord> getEmbargoRecords() {
        return embargoRecords;
    }

    public void setEmbargoRecords(List<EmbargoRecord> embargoRecords) {
        this.embargoRecords = embargoRecords;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public enum ErrorCode {
        RECORDS_NOT_FOUND("Could not find embargo records!"),
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
