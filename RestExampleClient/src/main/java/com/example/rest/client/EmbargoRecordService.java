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
    public EmbargoRecordListResponse getEmbargoRecordsForFirmId();

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
    @Path("/add")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public EmbargoRecordListResponse saveList(@Valid List<EmbargoRecord> embargoRecords);

    @POST
    @Path("/update")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public EmbargoRecordResponse updateEmbargoRecord(@Valid EmbargoRecord embargoRecord);

    @DELETE
    @Path("/delete/{Id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public DeleteEmbargoRecordResponse deleteEmbargoRecord(@PathParam("Id") Integer Id);

    @DELETE
    @Path("/delete/firm/{firmId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public DeleteEmbargoRecordResponse deleteEmbargoRecords(@PathParam("firmId") Integer firmId);

}
