package com.example.rest.server.web; /**
 * Created by Cristina
 * Date: 5/30/2017
 */
import com.example.rest.client.EmbargoRecordService;
import com.example.rest.client.model.EmbargoRecord;
import com.example.rest.client.response.DeleteEmbargoRecordResponse;
import com.example.rest.client.response.EmbargoRecordListResponse;
import com.example.rest.client.response.EmbargoRecordResponse;
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
    @Path("/get")
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
    @Path("/get/{firmId}")
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
    @Path("/get/{firmId}/{emailDomain}")
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
    @Path("/add")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public EmbargoRecordListResponse saveList(@Valid List<EmbargoRecord> embargoRecords) {
        LOGGER.info(String.format("Received POST request for [/embargo/add]"));

        DaoFactory daoFactory = null;
        List<EmbargoRecord> records = new ArrayList<EmbargoRecord>();
        if(!embargoRecords.isEmpty()){
            try {
                daoFactory = new DaoFactory();
                EmbargoRecordDao embargoRecordDao = daoFactory.createEmbargoDao();
                for(EmbargoRecord embargoRecord : embargoRecords){
                    EmbargoRecord record = embargoRecordDao.addEmbargoRecord(embargoRecord);
                    if (null == record) {
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

    @POST
    @Path("/update")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public EmbargoRecordResponse updateEmbargoRecord(@Valid EmbargoRecord embargoRecord) {
        LOGGER.info(String.format("Received POST request for [/embargo/update]"));

        DaoFactory daoFactory = null;
        if(embargoRecord != null){
            try {
                daoFactory = new DaoFactory();
                EmbargoRecordDao embargoRecordDao = daoFactory.createEmbargoDao();
                //get old embargo record
                EmbargoRecord oldRecord = embargoRecordDao.getEmbargoRecordById(embargoRecord.getId());
                if (oldRecord != null){
                    //add to backup table
                    EmbargoRecord backupRecord = embargoRecordDao.addEmbargoRecordBackup(oldRecord);
                    if (backupRecord == null){
                        LOGGER.info(String.format("Could not add backup record for id: [%d]", embargoRecord.getId()));
                    }
                    else {
                        LOGGER.info(String.format("Embargo backup record added for id: [%d]", embargoRecord.getId()));
                    }
                }
                    Boolean updated = embargoRecordDao.updateEmbargoRecord(embargoRecord);
                    if (!updated) {
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
    @Path("/delete/{Id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public DeleteEmbargoRecordResponse deleteEmbargoRecord(@PathParam("Id") Integer Id) {
        LOGGER.info(String.format("Received DELETE request for [/embargo/delete/%d]",Id));
        DaoFactory daoFactory = null;
        try {
            daoFactory = new DaoFactory();
            EmbargoRecordDao embargoRecordDao = daoFactory.createEmbargoDao();
            Boolean isRecordDeleted = embargoRecordDao.deleteEmbargoRecord(Id);
            if (isRecordDeleted) {
                LOGGER.info(String.format("Embargo record deleted for id: [%d]", Id));
                Boolean isBackupRecordDeleted = embargoRecordDao.deleteEmbargoRecordBackup(Id);
                if (isBackupRecordDeleted) {
                    return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.SuccessCode.RECORD_DELETED);
                }
                else {
                    LOGGER.error(String.format("Could not delete embargo backup record for id: [%d]", Id));
                    return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.SuccessCode.RECORD_DELETED);
                }
            }
            else {
                LOGGER.error(String.format("Could not delete embargo record for id: [%d]", Id));
                return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.ErrorCode.RECORD_NOT_DELETED);
            }
        } catch (Exception exception) {
            LOGGER.error("Exception using DaoFactory: ", exception);
            return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.ErrorCode.DATABASE_EXCEPTION);
        } finally {
            if (daoFactory != null) {
                daoFactory.close();
            }
        }
    }

    @DELETE
    @Path("/delete/firm/{firmId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public DeleteEmbargoRecordResponse deleteEmbargoRecords(@PathParam("firmId") Integer firmId) {
        LOGGER.info(String.format("Received DELETE request for [/embargo/delete/%d/]",firmId));
        DaoFactory daoFactory = null;
        try {
            daoFactory = new DaoFactory();
            EmbargoRecordDao embargoRecordDao = daoFactory.createEmbargoDao();
            Boolean recordsDeleted = embargoRecordDao.deleteEmbargoRecords(firmId);
            if (recordsDeleted) {
                LOGGER.info(String.format("Embargo records deleted for firmId: [%d]", firmId));
                Boolean backupRecordsDeleted = embargoRecordDao.deleteEmbargoRecordsBackup(firmId);
                if (backupRecordsDeleted) {
                    return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.SuccessCode.RECORDS_DELETED);
                }
                else {
                    LOGGER.error(String.format("Could not delete embargo backup records for id: [%d]", firmId));
                    return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.SuccessCode.RECORDS_DELETED);
                }
            }
            else {
                LOGGER.error(String.format("Could not delete embargo records for id: [%d]", firmId));
                return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.ErrorCode.RECORDS_NOT_DELETED);
            }
        } catch (Exception exception) {
            LOGGER.error("Exception using DaoFactory: ", exception);
            return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.ErrorCode.DATABASE_EXCEPTION);
        } finally {
            if (daoFactory != null) {
                daoFactory.close();
            }
        }
    }

}
