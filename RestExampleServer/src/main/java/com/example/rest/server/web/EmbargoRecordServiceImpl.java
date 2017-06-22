package com.example.rest.server.web; /**
 * Created by Cristina
 * Date: 5/30/2017
 */
import com.example.rest.client.EmbargoRecordService;
import com.example.rest.client.model.AuditEmbargoRecord;
import com.example.rest.client.model.EmbargoRecord;
import com.example.rest.client.response.AuditEmbargoRecordListResponse;
import com.example.rest.client.response.DeleteEmbargoRecordResponse;
import com.example.rest.client.response.EmbargoRecordListResponse;
import com.example.rest.client.response.EmbargoRecordResponse;
import com.example.rest.server.db.dao.AuditEmbargoRecordDao;
import com.example.rest.server.db.dao.DaoFactory;
import com.example.rest.server.db.dao.EmbargoRecordDao;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Path("/embargo")
public class EmbargoRecordServiceImpl implements EmbargoRecordService {
    private static final Logger LOGGER = Logger.getLogger(EmbargoRecordServiceImpl.class);

    @Inject
    public EmbargoRecordServiceImpl(){

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public EmbargoRecordListResponse getEmbargoRecords() {
        LOGGER.info(String.format("Received GET request for [/embargo]"));
        DaoFactory daoFactory = null;
        List<EmbargoRecord> embargoRecords = null;
        try {
            daoFactory = new DaoFactory();
            EmbargoRecordDao embargoRecordDao = daoFactory.createEmbargoDao();
            embargoRecords = embargoRecordDao.getEmbargoRecords();
            if (null == embargoRecords || embargoRecords.isEmpty()) {
                return new EmbargoRecordListResponse(EmbargoRecordListResponse.ErrorCode.RECORDS_NOT_FOUND);
            }
        } catch (Exception exception) {
            LOGGER.error("Exception using DaoFactory: ", exception);
            return new EmbargoRecordListResponse(EmbargoRecordListResponse.ErrorCode.DATABASE_EXCEPTION);
        } finally {
            if (daoFactory != null) {
                daoFactory.close();
            }
        }
        return new EmbargoRecordListResponse(embargoRecords);
    }


    @GET
    @Path("/{firmId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public EmbargoRecordListResponse getEmbargoRecordsForFirmId(@PathParam("firmId") Integer firmId) {
        LOGGER.info(String.format("Received GET request for [/embargo/%d]", firmId));
        DaoFactory daoFactory = null;
        List<EmbargoRecord> embargoRecords = null;
        try {
            daoFactory = new DaoFactory();
            EmbargoRecordDao embargoRecordDao = daoFactory.createEmbargoDao();
            embargoRecords = embargoRecordDao.getEmbargoRecordsForFirmId(firmId);
            if (null == embargoRecords || embargoRecords.isEmpty()) {
                return new EmbargoRecordListResponse(EmbargoRecordListResponse.ErrorCode.RECORDS_NOT_FOUND);
            }
        } catch (Exception exception) {
            LOGGER.error("Exception using DaoFactory: ", exception);
            return new EmbargoRecordListResponse(EmbargoRecordListResponse.ErrorCode.DATABASE_EXCEPTION);
        } finally {
            if (daoFactory != null) {
                daoFactory.close();
            }
        }
        return new EmbargoRecordListResponse(embargoRecords);
    }

    @GET
    @Path("/{firmId}/{emailDomain}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public EmbargoRecordResponse getEmbargoRecord(@PathParam("firmId") Integer firmId,
                                                  @PathParam("emailDomain") String emailDomain) {
        LOGGER.info(String.format("Received GET request for [/embargo/%d/%s]", firmId, emailDomain));
        DaoFactory daoFactory = null;
        EmbargoRecord embargoRecord = null;
        try {
            daoFactory = new DaoFactory();
            EmbargoRecordDao embargoRecordDao = daoFactory.createEmbargoDao();
            embargoRecord = embargoRecordDao.getEmbargoRecord(firmId, emailDomain);
            if (null == embargoRecord) {
                return new EmbargoRecordResponse(EmbargoRecordResponse.ErrorCode.RECORD_NOT_FOUND);
            }
        } catch (Exception exception) {
            LOGGER.error("Exception using DaoFactory: ", exception);
            return new EmbargoRecordResponse(EmbargoRecordResponse.ErrorCode.DATABASE_EXCEPTION);
        } finally {
            if (daoFactory != null) {
                daoFactory.close();
            }
        }
        return new EmbargoRecordResponse(embargoRecord);
    }

    @POST
    @Path("/{source}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public EmbargoRecordListResponse saveList(@Valid List<EmbargoRecord> embargoRecords,
                                              @PathParam("source") String source,
                                              @HeaderParam("userEmail") String userEmail) {
        LOGGER.info(String.format("Received POST request for [/embargo]"));

        DaoFactory daoFactory = null;
        List<EmbargoRecord> records = new ArrayList<EmbargoRecord>();
        if(!embargoRecords.isEmpty()){
            try {
                daoFactory = new DaoFactory();
                EmbargoRecordDao embargoRecordDao = daoFactory.createEmbargoDao();
                for(EmbargoRecord embargoRecord : embargoRecords){
                    EmbargoRecord record = embargoRecordDao.addEmbargoRecord(embargoRecord);
                    //Create audit embargo record
                    AuditEmbargoRecord auditEmbargoRecord = createAuditEmbargoRecord(embargoRecord,source,userEmail,"Added", embargoRecordDao.getLastInsertedId());
                    if (null == record) {
                        return new EmbargoRecordListResponse(EmbargoRecordListResponse.ErrorCode.DATABASE_EXCEPTION);
                    }
                    AuditEmbargoRecordDao auditEmbargoRecordDao = daoFactory.createAuditEmbargoDao();
                    AuditEmbargoRecord auditRecord = auditEmbargoRecordDao.addAuditEmbargoRecord(auditEmbargoRecord);
                    if (auditRecord == null){
                        return new EmbargoRecordListResponse(EmbargoRecordListResponse.ErrorCode.DATABASE_EXCEPTION);
                    }
                    records.add(record);
                }
                return new EmbargoRecordListResponse(records, null);
            } catch (Exception exception) {
                return new EmbargoRecordListResponse(EmbargoRecordListResponse.ErrorCode.DATABASE_EXCEPTION);
            }finally {
                if(null != daoFactory){
                    daoFactory.close();
                }
            }
        }
        return new EmbargoRecordListResponse(null, EmbargoRecordListResponse.ErrorCode.RECORDS_NOT_FOUND);
    }

    @PUT
    @Path("/{source}/{Id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public EmbargoRecordResponse updateEmbargoRecord(@Valid EmbargoRecord embargoRecord,
                                                     @PathParam("source") String source,
                                                     @PathParam("Id") Integer Id,
                                                     @HeaderParam("userEmail") String userEmail){
        LOGGER.info(String.format("Received PUT request for [/embargo]"));

        DaoFactory daoFactory = null;
        if(embargoRecord != null){
            try {
                daoFactory = new DaoFactory();
                EmbargoRecordDao embargoRecordDao = daoFactory.createEmbargoDao();
                //Create audit embargo record
                AuditEmbargoRecord auditEmbargoRecord = createAuditEmbargoRecord(embargoRecord, source, userEmail, "Updated", Id);

                Boolean updated = embargoRecordDao.updateEmbargoRecord(embargoRecord);
                if (!updated) {
                    return new EmbargoRecordResponse(EmbargoRecordResponse.ErrorCode.DATABASE_EXCEPTION);
                }
                //add to audit table
                AuditEmbargoRecordDao auditEmbargoRecordDao = daoFactory.createAuditEmbargoDao();
                AuditEmbargoRecord auditRecord = auditEmbargoRecordDao.addAuditEmbargoRecord(auditEmbargoRecord);
                if (auditRecord == null){
                    return new EmbargoRecordResponse(EmbargoRecordResponse.ErrorCode.DATABASE_EXCEPTION);
                }
                return new EmbargoRecordResponse(embargoRecord, null);
            } catch (Exception exception) {
                return new EmbargoRecordResponse(EmbargoRecordResponse.ErrorCode.DATABASE_EXCEPTION);
            }finally {
                if(null != daoFactory){
                    daoFactory.close();
                }
            }
        }
        return new EmbargoRecordResponse(null, EmbargoRecordResponse.ErrorCode.RECORD_NOT_FOUND);
    }

    @DELETE
    @Path("/{source}/{Id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public DeleteEmbargoRecordResponse deleteEmbargoRecord(@PathParam("Id") Integer Id,
                                                           @PathParam("source") String source,
                                                           @HeaderParam("userEmail") String userEmail) {

        LOGGER.info(String.format("Received DELETE request for [/embargo/%s/%d]",source, Id));
        if (deleteRecordAndAddAuditEntry(Id, source, userEmail)){
            return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.SuccessCode.RECORD_DELETED);
        }
        return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.ErrorCode.RECORD_NOT_DELETED);
    }

    @DELETE
    @Path("/{source}/firm/{firmId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public DeleteEmbargoRecordResponse deleteEmbargoRecords(@PathParam("firmId") Integer firmId,
                                                            @PathParam("source") String source,
                                                            @HeaderParam("userEmail") String userEmail) {
        LOGGER.info(String.format("Received DELETE request for [/embargo/%s/firm/%d/]", source, firmId));
        DaoFactory daoFactory = null;
        try {
            daoFactory = new DaoFactory();
            EmbargoRecordDao embargoRecordDao = daoFactory.createEmbargoDao();
            List<EmbargoRecord> recordList = embargoRecordDao.getEmbargoRecordsForFirmId(firmId);
            if (recordList!=null && !recordList.isEmpty()){
                for (EmbargoRecord record: recordList){
                    if (!deleteRecordAndAddAuditEntry(record.getId(), source, userEmail)){
                        return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.ErrorCode.RECORDS_NOT_DELETED);
                    }
                }
                return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.SuccessCode.RECORDS_DELETED);
            }
        }catch (Exception exception){
            return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.ErrorCode.DATABASE_EXCEPTION);
        }
        return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.ErrorCode.RECORDS_NOT_DELETED);
    }

    private Boolean deleteRecordAndAddAuditEntry(Integer Id, String source, String userEmail){
        DaoFactory daoFactory = null;
        try {
            daoFactory = new DaoFactory();
            EmbargoRecordDao embargoRecordDao = daoFactory.createEmbargoDao();
            EmbargoRecord embargoRecord = embargoRecordDao.getEmbargoRecordById(Id);
            Boolean isRecordDeleted = embargoRecordDao.deleteEmbargoRecord(Id);
            if (isRecordDeleted) {
                LOGGER.info(String.format("Embargo record deleted for id: [%d]", Id));
                //Create audit embargo record
                AuditEmbargoRecord auditEmbargoRecord = createAuditEmbargoRecord(embargoRecord, source, userEmail, "Deleted", Id);
                //add to audit table
                AuditEmbargoRecordDao auditEmbargoRecordDao = daoFactory.createAuditEmbargoDao();
                AuditEmbargoRecord auditRecord = auditEmbargoRecordDao.addAuditEmbargoRecord(auditEmbargoRecord);
                return true;
            }
            else {
                LOGGER.error(String.format("Could not delete embargo record for id: [%d]", Id));
                return false;
            }
        } catch (Exception exception) {
            LOGGER.error("Exception using DaoFactory: ", exception);
            return false;
        } finally {
            if (daoFactory != null) {
                daoFactory.close();
            }
        }
    }

    private AuditEmbargoRecord createAuditEmbargoRecord(EmbargoRecord embargoRecord, String source, String userEmail, String action, Integer Id){
        AuditEmbargoRecord auditEmbargoRecord = new AuditEmbargoRecord();
        auditEmbargoRecord.setFirmId(embargoRecord.getFirmId());
        auditEmbargoRecord.setEmailDomain(embargoRecord.getEmailDomain());
        auditEmbargoRecord.setSource(source);
        auditEmbargoRecord.setUserEmail(userEmail);
        auditEmbargoRecord.setFullAnonDays(embargoRecord.getFullAnonDays());
        auditEmbargoRecord.setFirmVisibleDays(embargoRecord.getFirmVisibleDays());
        auditEmbargoRecord.setFullVisibleDays(embargoRecord.getFullVisibleDays());
        auditEmbargoRecord.setAction(action + " record");
        auditEmbargoRecord.setRecordId(Id);
        return auditEmbargoRecord;
    }

    @GET
    @Path("/audit/{Id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public AuditEmbargoRecordListResponse getAuEmbargoRecordListResponse(@PathParam("Id") Integer Id) {
        LOGGER.info(String.format("Received GET request for [/embargo/%d]", Id));
        DaoFactory daoFactory = null;
        List<AuditEmbargoRecord> auditEmbargoRecords = null;
        try {
            daoFactory = new DaoFactory();
            AuditEmbargoRecordDao auditEmbargoRecordDao = daoFactory.createAuditEmbargoDao();
            auditEmbargoRecords = auditEmbargoRecordDao.getAuditEmbargoRecordsForId(Id);
            if (null == auditEmbargoRecords || auditEmbargoRecords.isEmpty()) {
                return new AuditEmbargoRecordListResponse(AuditEmbargoRecordListResponse.ErrorCode.RECORDS_NOT_FOUND);
            }
        } catch (Exception exception) {
            LOGGER.error("Exception using DaoFactory: ", exception);
            return new AuditEmbargoRecordListResponse(AuditEmbargoRecordListResponse.ErrorCode.DATABASE_EXCEPTION);
        } finally {
            if (daoFactory != null) {
                daoFactory.close();
            }
        }
        return new AuditEmbargoRecordListResponse(auditEmbargoRecords);
    }
}
