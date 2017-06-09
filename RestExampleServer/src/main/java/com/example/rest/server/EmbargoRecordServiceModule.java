/**
 * Created by Cristina
 * Date: 6/8/2017.
 */
package com.example.rest.server;

import com.example.rest.client.EmbargoRecordService;
import com.example.rest.server.web.EmbargoRecordServiceImpl;
import com.google.inject.AbstractModule;

public class EmbargoRecordServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EmbargoRecordService.class).to(EmbargoRecordServiceImpl.class);
    }
}
