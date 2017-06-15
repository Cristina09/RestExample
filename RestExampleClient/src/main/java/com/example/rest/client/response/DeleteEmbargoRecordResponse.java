/**
 * Created by Cristina
 * Date: 6/7/2017.
 */
package com.example.rest.client.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeleteEmbargoRecordResponse {

    private ErrorCode errorCode;
    private SuccessCode successCode;

    public DeleteEmbargoRecordResponse(){

    }

    public DeleteEmbargoRecordResponse(ErrorCode errorCode, SuccessCode successCode) {
        this.errorCode = errorCode;
        this.successCode = successCode;
    }

    public DeleteEmbargoRecordResponse(ErrorCode errorCode){
        this(errorCode, null);
    }

    public DeleteEmbargoRecordResponse(SuccessCode successCode){
        this(null, successCode);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public SuccessCode getSuccessCode() {
        return successCode;
    }

    public void setSuccessCode(SuccessCode successCode) {
        this.successCode = successCode;
    }

    public enum ErrorCode {
        RECORD_NOT_DELETED("Could not delete embargo record!"),
        RECORDS_NOT_DELETED("Could not delete embargo records!"),
        DATABASE_EXCEPTION("Could not initiate DaoFactory!"),;

        private final String message;

        ErrorCode(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public enum SuccessCode {
        RECORDS_DELETED("Embargo records successfully deleted"),
        RECORD_DELETED("Embargo record successfully deleted!"),;

        private final String message;

        SuccessCode(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
