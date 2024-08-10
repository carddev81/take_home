/**
 * 
 */
package org.unlockedlabs.pkrates.ws.api.core;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Jersey Application initial setup and configuration.
 * 
 * @author Richard Salas
 * @version 1.0.0
 */
public class Application extends ResourceConfig {

    /**
     * Creates an instance of the the Application
     */
    public Application() {
        packages("org.unlockedlabs.pkrates.ws.api.endpoints")
        .register(JacksonContextResolver.class)// JSON processing (1)
        .register(JacksonFeature.class)// JSON processing (2)
        .register(CorsRepsonseFilter.class)//CORS responses (cross - origin)
        .register(MultiPartFeature.class)
        .register(ZonedDateTimeConverterProvider.class);//custom class for converting zoned date/time string
    }// end method

}//end class
