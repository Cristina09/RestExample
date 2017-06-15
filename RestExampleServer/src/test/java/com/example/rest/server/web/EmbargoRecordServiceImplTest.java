package com.example.rest.server.web;

import com.example.rest.client.EmbargoRecordService;
import com.example.rest.client.response.EmbargoRecordResponse;
import com.example.rest.server.EmbargoRecordServiceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

public class EmbargoRecordServiceImplTest {

    @Test
    public void testGetEmbargoRecord() throws Exception {
//        ResteasyClient client = new ResteasyClientBuilder().build();
//        ResteasyWebTarget target = client.target(UriBuilder.fromPath("http://localhost:8083/embargo/get/100/cristina.com"));
//
//        Response response = target.request(MediaType.APPLICATION_JSON).get();
//        EmbargoRecordResponse embargoRecord = response.readEntity(EmbargoRecordResponse.class);
//        assertNotNull(embargoRecord);
//        assertNull(embargoRecord.getErrorCode());
    }

    @Test
    public void testRequest() throws Exception {
//        ResteasyClient client = new ResteasyClientBuilder().build();
//        ResteasyWebTarget target = client.target("http://localhost:8083/embargo/get");
//        EmbargoRecordService embargoRecordService = target.proxy(EmbargoRecordService.class);
//        EmbargoRecordResponse response = embargoRecordService.getEmbargoRecord(1, "cristina.com");

  //        ResteasyClient client = new ResteasyClientBuilder().build();
////      client.register(new RestHeaderFilter("Authorization", "Basic Ymx1ZW1hdHJpeDplbWJhcmdvU2VydmljZVBhc3M="));
   //       ResteasyWebTarget target = client.target(UriBuilder.fromPath("http://localhost:8083/embargo/get"));

   //       EmbargoRecordService service = target.proxy(EmbargoRecordService.class);

   //       EmbargoRecordResponse response = service.getEmbargoRecord(1, "cristina.com");
//        EmbargoRecordResponse response = service.getTest();
//        assertNotNull(response);


        Injector injector = Guice.createInjector(new EmbargoRecordServiceModule());

        EmbargoRecordService ers = injector.getInstance(EmbargoRecordService.class);
        EmbargoRecordResponse response = ers.getEmbargoRecord(1, "cristina.com");

//        Client client = ClientBuilder.newClient();
//        WebTarget target = client.target("http://localhost:8083/embargo/get");
//        Response list = target.request().method("getEmbargoRecords");

    }
}