/**
 * Created by Cristina
 * Date: 6/6/2017
 */
package com.example.rest.server.web.filter;

import com.example.rest.server.web.RestServlet;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Base64;

public class AuthenticationFilter implements ContainerRequestFilter {

    static final String BASIC_AUTH_HEADER = "Authorization";
    static final String BASIC_AUTH_TOKEN_PREFIX = "Basic ";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        MultivaluedMap<String, String> headers = requestContext.getHeaders();
        String internalAuthToken = RestServlet.getAuthenticationToken();


        String basicAuthToken = headers.getFirst(BASIC_AUTH_HEADER);
        if (basicAuthToken == null) {
            reject(requestContext);
            return;
        }

        int idx = basicAuthToken.indexOf(BASIC_AUTH_TOKEN_PREFIX);
        if (idx < 0) {
            reject(requestContext);
            return;
        }
        int start = idx + BASIC_AUTH_TOKEN_PREFIX.length();
        if (start >= basicAuthToken.length()) {
            reject(requestContext);
            return;
        }

        byte[] basicAuthTokenBytes;
        try {
            basicAuthTokenBytes = Base64.getDecoder().decode(basicAuthToken.substring(start));
        } catch (IllegalArgumentException e) {
            reject(requestContext);
            return;
        }
        String basicAuthTokenString = new String(basicAuthTokenBytes, "UTF-8");
        if(!internalAuthToken.equals(basicAuthTokenString)){
            reject(requestContext);
        }
    }

    void reject(ContainerRequestContext requestContext) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header("WWW-Authenticate", "Basic realm=\"User Query REST API\"")
                        .build());
    }
}
