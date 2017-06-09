package com.example.rest.client.HeaderFilter;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

/**
 * Created by Cristina on 6/8/2017.
 */
public class RestHeaderFilter implements ClientRequestFilter{
    private final String name;
    private final String value;

    public RestHeaderFilter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void filter(ClientRequestContext clientRequestContext) throws IOException {
        clientRequestContext.getHeaders().add(name, value);
    }
}
