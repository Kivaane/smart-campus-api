package com.smartcampus.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    
    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());
    private static final String START_TIME_PROPERTY = "request.start.time";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty(START_TIME_PROPERTY, System.currentTimeMillis());
        
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();
        
        LOGGER.info(String.format("→ REQUEST: %s %s", method, path));
    }

    @Override
    public void filter(ContainerRequestContext requestContext, 
                      ContainerResponseContext responseContext) throws IOException {
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();
        int status = responseContext.getStatus();
        
        Long startTime = (Long) requestContext.getProperty(START_TIME_PROPERTY);
        long duration = System.currentTimeMillis() - startTime;
        
        LOGGER.info(String.format("← RESPONSE: %s %s -> %d (%dms)", 
                                  method, path, status, duration));
    }
}
