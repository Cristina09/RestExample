/**
 * Created by Cristina
 * Date: 6/13/2017.
 */
package com.example.rest.server.web.filter.ServletContextListener;

import com.example.rest.server.EmbargoRecordServiceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceServletConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new EmbargoRecordServiceModule());
    }
}
