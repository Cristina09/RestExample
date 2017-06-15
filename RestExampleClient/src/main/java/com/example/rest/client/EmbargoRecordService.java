package com.example.rest.client;

import com.example.rest.client.annotations.RequireInternal;
import com.example.rest.client.model.EmbargoRecord;
import com.example.rest.client.response.DeleteEmbargoRecordResponse;
import com.example.rest.client.response.EmbargoRecordListResponse;
import com.example.rest.client.response.EmbargoRecordResponse;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Cristina on 5/31/2017.
 */

@Path("/embargo")
@RequireInternal
public interface EmbargoRecordService {

    @GET
    @Path("/get")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public EmbargoRecordListResponse getEmbargoRecords();

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public EmbargoRecordResponse getTest();

    @GET
    @Path("/get/{firmId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public EmbargoRecordListResponse getEmbargoRecordsForFirmId(@PathParam("firmId") Integer firmId);

    @GET
    @Path("/get/{firmId}/{emailDomain}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public EmbargoRecordResponse getEmbargoRecord(@PathParam("firmId") Integer firmId,
                                                  @PathParam("emailDomain") String emailDomain);

    @POST
    @Path("/add/{firmId}/{emailDomain}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public EmbargoRecordResponse save(@Valid EmbargoRecord embargoRecord,
                                      @PathParam("firmId") Integer firmId,
                                      @PathParam("emailDomain") String emailDomain);

    @POST
    @Path("/add")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public EmbargoRecordListResponse saveList(@Valid List<EmbargoRecord> embargoRecords);

    @DELETE
    @Path("/delete/{firmId}/{emailDomain}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public DeleteEmbargoRecordResponse deleteEmbargoRecord(@PathParam("firmId") Integer firmId,
                                                           @PathParam("emailDomain") String emailDomain);

    @DELETE
    @Path("/delete/{firmId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public DeleteEmbargoRecordResponse deleteEmbargoRecord(@PathParam("firmId") Integer firmId);

}
