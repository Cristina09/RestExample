/**
 * Created by Cristina
 * Date: 6/6/2017.
 */
package com.example.rest.client.response;

import com.example.rest.client.model.EmbargoRecord;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmbargoRecordResponse {
    private EmbargoRecord embargoRecord;
    private ErrorCode errorCode;

    private EmbargoRecordResponse() {
    }

    public EmbargoRecordResponse(EmbargoRecord embargoRecord){
        this(embargoRecord, null);
    }

    public EmbargoRecordResponse(ErrorCode errorCode){
        this(null, errorCode);
    }

    public EmbargoRecordResponse(EmbargoRecord embargoRecord, ErrorCode errorCode){
        this.embargoRecord = embargoRecord;
        this.errorCode = errorCode;
    }

    public EmbargoRecord getEmbargoRecord() {
        return embargoRecord;
    }

    public void setEmbargoRecord(EmbargoRecord embargoRecord) {
        this.embargoRecord = embargoRecord;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public enum ErrorCode {
        RECORD_NOT_FOUND("Could not find embargo record!"),
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
