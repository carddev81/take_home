/**
 * 
 */
package org.unlockedlabs.pkrates.ws.api.core;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Cross-origin resource sharing ContainerResponseFilter to set common headers for allowing specific Request methods, headers, and allowing access from anywhere.
 * 
 * @author Richard Salas
 * @version 1.0.0
 */
@Provider
public class CorsRepsonseFilter implements ContainerResponseFilter{

    /**
     * {@inheritDoc}
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }//end method

}//end class
