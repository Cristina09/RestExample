/**
 * Created by Cristina
 * Date: 5/30/2017
 */
package com.example.rest.server;

import com.example.rest.server.web.EmbargoRecordServiceImpl;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class RestApplication extends Application {

    private Set<Object> singletons = new HashSet<Object>();

    public RestApplication() {
        singletons.add(new EmbargoRecordServiceImpl());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
