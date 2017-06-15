/**
 * Created by Cristina
 * Date: 6/8/2017.
 */
package com.example.rest.server;

import com.example.rest.client.EmbargoRecordService;
import com.example.rest.server.web.EmbargoRecordServiceImpl;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

public class EmbargoRecordServiceModule extends ServletModule{
    @Override
    protected void configureServlets() {
        // Bind these to Guice's own RequestScoped scope.
        // Also see RESTEasy's RequestScopeModule, since this is based off it.
        bind(Request.class).toProvider(new ResteasyContextProvider<>(Request.class)).in(RequestScoped.class);
        bind(HttpHeaders.class).toProvider(new ResteasyContextProvider<>(HttpHeaders.class)).in(RequestScoped.class);
        bind(UriInfo.class).toProvider(new ResteasyContextProvider<>(UriInfo.class)).in(RequestScoped.class);
        bind(SecurityContext.class).toProvider(new ResteasyContextProvider<>(SecurityContext.class)).in(RequestScoped.class);

        // This needs to be bound so that it will inject our current injector instead of creating a new one.
        bind(GuiceResteasyBootstrapServletContextListener.class);

        // This needs to be bound in order for RESTEasy to use them
        bind(EmbargoRecordService.class).to(EmbargoRecordServiceImpl.class);
    }

    private static class ResteasyContextProvider<T> implements Provider<T> {
        private final Class<T> instanceClass;

        private ResteasyContextProvider(Class<T> instanceClass) {
            this.instanceClass = instanceClass;
        }

        @Override
        public T get() {
            return ResteasyProviderFactory.getContextData(instanceClass);
        }
    }
}
