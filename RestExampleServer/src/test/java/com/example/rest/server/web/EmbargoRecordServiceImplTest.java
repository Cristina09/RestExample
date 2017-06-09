package com.example.rest.server.web;

import com.example.rest.client.EmbargoRecordService;
import com.example.rest.client.response.EmbargoRecordResponse;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class EmbargoRecordServiceImplTest {

    @Test
    public void testGetEmbargoRecord() throws Exception {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(UriBuilder.fromPath("http://localhost:8083/embargo/get/100/cristina.com"));

        Response response = target.request(MediaType.APPLICATION_JSON).get();
        EmbargoRecordResponse embargoRecord = response.readEntity(EmbargoRecordResponse.class);
        assertNotNull(embargoRecord);
        assertNull(embargoRecord.getErrorCode());
    }

    @Test
    public void testRequest() throws Exception {
        ResteasyClient client = new ResteasyClientBuilder().build();
        //client.register(new RestHeaderFilter("Authorization", "Basic Ymx1ZW1hdHJpeDplbWJhcmdvU2VydmljZVBhc3M="));
        ResteasyWebTarget target = client.target(UriBuilder.fromPath("http://localhost:8083/embargo"));

        EmbargoRecordService service = target.proxy(EmbargoRecordService.class);

        //EmbargoRecordResponse response = service.getEmbargoRecord(1,"cristina.com");
        EmbargoRecordResponse response = service.getTest();
        assertNotNull(response);


//        Injector injector = Guice.createInjector(new EmbargoRecordServiceModule());
//
//        EmbargoRecordService ers = injector.getInstance(EmbargoRecordService.class);
//        EmbargoRecordResponse response = ers.getEmbargoRecord(1, "cristina.com");
    }
}