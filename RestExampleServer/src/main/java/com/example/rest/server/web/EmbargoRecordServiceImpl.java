/**
 * Created by Cristina
 * Date: 5/30/2017
 */
package com.example.rest.server.web;

import com.example.rest.client.EmbargoRecordService;
import com.example.rest.client.model.EmbargoRecord;
import com.example.rest.client.response.DeleteEmbargoRecordResponse;
import com.example.rest.client.response.EmbargoRecordListResponse;
import com.example.rest.client.response.EmbargoRecordResponse;
import com.example.rest.server.db.dao.DaoFactory;
import com.example.rest.server.db.dao.EmbargoRecordDao;
import org.apache.log4j.Logger;
import com.google.inject.Inject;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/embargo")
public class EmbargoRecordServiceImpl implements EmbargoRecordService {
    private static final Logger LOGGER = Logger.getLogger(EmbargoRecordServiceImpl.class);

    @Inject
    public EmbargoRecordServiceImpl(){

    }

    @GET
    @Path("/get")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public EmbargoRecordListResponse getEmbargoRecordsForFirmId() {
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
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public EmbargoRecordResponse getTest() {
        return null;
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
    @Path("/add/{firmId}/{emailDomain}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public EmbargoRecordResponse save(@Valid EmbargoRecord embargoRecord,
                                      @PathParam("firmId") Integer firmId,
                                      @PathParam("emailDomain") String emailDomain) {
        LOGGER.info(String.format("Received POST request for [/embargo/%d/%s]", firmId, emailDomain));

        DaoFactory daoFactory = null;
        try {
            daoFactory = new DaoFactory();
            EmbargoRecordDao embargoRecordDao = daoFactory.createEmbargoDao();
            EmbargoRecord record = embargoRecordDao.addEmbargoRecord(embargoRecord);
            if (null == record) {
                return new EmbargoRecordResponse(EmbargoRecordResponse.ErrorCode.DATABASE_EXCEPTION);
            }
            return new EmbargoRecordResponse(record, null);
        } catch (Exception exception) {
            return new EmbargoRecordResponse(EmbargoRecordResponse.ErrorCode.DATABASE_EXCEPTION);
        }finally {
            if(null != daoFactory){
                daoFactory.close();
            }
        }
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

    @DELETE
    @Path("/delete/{firmId}/{emailDomain}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public DeleteEmbargoRecordResponse deleteEmbargoRecord(@PathParam("firmId") Integer firmId,
                                                           @PathParam("emailDomain") String emailDomain) {
        LOGGER.info(String.format("Received DELETE request for [/embargo/delete/%d/%s]",firmId, emailDomain));
        DaoFactory daoFactory = null;
        try {
            daoFactory = new DaoFactory();
            EmbargoRecordDao embargoRecordDao = daoFactory.createEmbargoDao();
            Boolean isRecordDeleted = embargoRecordDao.deleteEmbargoRecord(firmId, emailDomain);
            if (isRecordDeleted) {
                return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.SuccessCode.RECORD_DELETED);
            }
            return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.ErrorCode.RECORD_NOT_DELETED);
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
    @Path("/delete/{firmId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public DeleteEmbargoRecordResponse deleteEmbargoRecord(@PathParam("firmId") Integer firmId) {
        LOGGER.info(String.format("Received DELETE request for [/embargo/delete/%d/]",firmId));
        DaoFactory daoFactory = null;
        try {
            daoFactory = new DaoFactory();
            EmbargoRecordDao embargoRecordDao = daoFactory.createEmbargoDao();
            Boolean isRecordDeleted = embargoRecordDao.deleteEmbargoRecords(firmId);
            if (isRecordDeleted) {
                return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.SuccessCode.RECORDS_DELETED);
            }
            return new DeleteEmbargoRecordResponse(DeleteEmbargoRecordResponse.ErrorCode.RECORD_NOT_DELETED);
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
